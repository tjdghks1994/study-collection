package com.example.settlement.service;

import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.settlement.entity.Settlement;
import com.example.settlement.repository.SettlementRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SettlementScheduledTasks {

    public static final String PAYMENT_COMPLETED = "paid";
    private final PaymentRepository paymentRepository;
    private final SettlementRepository settlementRepository;
    private final JdbcTemplate jdbcTemplate;

    public SettlementScheduledTasks(PaymentRepository paymentRepository,
                                    SettlementRepository settlementRepository,
                                    JdbcTemplate jdbcTemplate) {
        this.paymentRepository = paymentRepository;
        this.settlementRepository = settlementRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "0 * * * * ?")
    @SchedulerLock(name = "ScheduledTask_run")
    public void dailySettlement() {
        // 어제 날짜
        LocalDate yesterday = LocalDate.of(2024,9,6).minusDays(1);
        // 어제의 시작 시각 설정
        LocalDateTime startDate = yesterday.atStartOfDay();
        // 어제의 끝 시각 설정
        LocalDateTime endDate = yesterday.atTime(LocalTime.of(23, 59, 59));

        // 해당 기간 동안의 결제 내역 조회 및 집계
        Map<Long, BigDecimal> settlementMap = getSettlementMap(startDate, endDate);

        long beforeTime = System.currentTimeMillis();
//        processSettlements(settlementMap, yesterday);
        bulkProcessSettlements(settlementMap, yesterday);
        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        log.info("실행 시간(ms) = {}", diffTime);
        log.info("집계 데이터 삽입 완료!");
    }

    private Map<Long, BigDecimal> getSettlementMap(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> paymentList = paymentRepository.findByPaymentDateBetweenAndStatus(startDate, endDate, PAYMENT_COMPLETED);
        // partner_id 를 기준으로 group by
        return paymentList.parallelStream()
                .collect(Collectors.groupingBy(
                        Payment::getPartnerId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Payment::getPaymentAmount,
                                BigDecimal::add
                        )
                ));
    }

    private void processSettlements(Map<Long, BigDecimal> settlementMap, LocalDate paymentDate) {
        ForkJoinPool customForkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1);

        try {
            customForkJoinPool.submit(() ->
                    settlementMap.entrySet().stream()
                            .forEach(entry -> {
                                Settlement settlement = Settlement.create(entry.getKey(), entry.getValue(), paymentDate);
                                settlementRepository.save(settlement);
                            })
            ).get();

        } catch (Exception e) {
            log.error("실패", e.getMessage());
            // 필요하다면 예외 이력을 저장하는 db 로직 추가
        } finally {
            customForkJoinPool.shutdown();  // 자원 해제
        }

    }

    private void bulkProcessSettlements(Map<Long, BigDecimal> settlementMap, LocalDate paymentDate) {
        String sql = "INSERT INTO settlements (partner_id, total_amount, payment_date) VALUES (?, ?, ?)";

        List<Long> failedPartnerIds = new ArrayList<>();

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Long partnerId = (Long) settlementMap.keySet().toArray()[i];
                    BigDecimal amount = settlementMap.get(partnerId);

                    ps.setLong(1, partnerId);
                    ps.setBigDecimal(2, amount);
                    ps.setObject(3, paymentDate);
                }

                @Override
                public int getBatchSize() {
                    return 1000;
                }
            });
        } catch (DataAccessException e) {
            Throwable cause = e.getCause();
            if (cause instanceof BatchUpdateException) {
                BatchUpdateException batchEx = (BatchUpdateException) cause;
                int[] updateCounts = batchEx.getUpdateCounts();

                // 실패한 인덱스만 추출
                int idx = 0;
                for (Map.Entry<Long, BigDecimal> entry : settlementMap.entrySet()) {
                    if (updateCounts[idx] == Statement.EXECUTE_FAILED) {    // 실패한 경우
                        failedPartnerIds.add(entry.getKey());
                    }
                    idx++;
                }

                log.error("Failed Partner Id = {}", failedPartnerIds);
            }

            throw e;
        }

    }
}
