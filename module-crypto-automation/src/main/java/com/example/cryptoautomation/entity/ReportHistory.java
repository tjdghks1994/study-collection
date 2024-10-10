package com.example.cryptoautomation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "report_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String market;
    @Column
    private String price;
    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @Builder
    private ReportHistory(String market, String price) {
        this.market = market;
        this.price = price;
        this.reportedAt = LocalDateTime.now();
    }

    public static ReportHistory of(String market, String price) {
        return ReportHistory.builder()
                .market(market)
                .price(price)
                .build();
    }
}
