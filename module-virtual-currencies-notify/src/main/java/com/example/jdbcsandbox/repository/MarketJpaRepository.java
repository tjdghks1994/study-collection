package com.example.jdbcsandbox.repository;

import com.example.jdbcsandbox.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketJpaRepository extends JpaRepository<Market, Long> {
    List<Market> findBySymbol(String marketSymbol);
}
