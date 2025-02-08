package com.example.settlement.service;

import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.settlement.entity.Settlement;
import com.example.settlement.repository.SettlementRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SettlementScheduledTasks {

    public static final String PAYMENT_COMPLETED = "paid";
    private final PaymentRepository paymentRepository;
    private final SettlementRepository settlementRepository;

    public SettlementScheduledTasks(PaymentRepository paymentRepository, SettlementRepository settlementRepository) {
        this.paymentRepository = paymentRepository;
        this.settlementRepository = settlementRepository;
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

        processSettlements(settlementMap, yesterday);
        log.info("집계 데이터 삽입 완료!");
    }

    private Map<Long, BigDecimal> getSettlementMap(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> paymentList = paymentRepository.findByPaymentDateBetweenAndStatus(startDate, endDate, PAYMENT_COMPLETED);
        // partner_id 를 기준으로 group by
        return paymentList.stream()
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
        settlementMap.entrySet().stream()
                .forEach(entry->{
                    Settlement settlement = Settlement.create(entry.getKey(), entry.getValue(), paymentDate);
                    settlementRepository.save(settlement);
                });
    }
}
