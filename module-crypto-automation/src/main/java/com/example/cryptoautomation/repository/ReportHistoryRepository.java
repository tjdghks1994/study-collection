package com.example.cryptoautomation.repository;

import com.example.cryptoautomation.entity.ReportHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportHistoryRepository {
    private final ReportHistoryJpaRepository repository;

    public void save(String market, String price) {
        repository.save(ReportHistory.of(market, price));
    }
}
