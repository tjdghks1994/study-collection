package com.example.jdbcsandbox.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MARKETS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Market {

    @Id
    @Column(name = "market_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "market_symbol")
    private String symbol;
    @Column(name = "market_korean_name")
    private String koreanName;
    @Column(name = "market_english_name")
    private String englishName;
    @Column(name = "market_warning")
    private String marketWarning;
}
