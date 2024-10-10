package com.example.cryptoautomation.repository;

import com.example.cryptoautomation.entity.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportHistoryJpaRepository extends JpaRepository<ReportHistory, Integer> {
}
