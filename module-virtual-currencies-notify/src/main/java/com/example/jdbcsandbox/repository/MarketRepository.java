package com.example.jdbcsandbox.repository;

import com.example.jdbcsandbox.domain.Market;
import com.example.jdbcsandbox.domain.QMarket;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MarketRepository {
    private final MarketJpaRepository marketJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public MarketRepository(MarketJpaRepository marketJpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.marketJpaRepository = marketJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Market queryDsl(String market) {
        QMarket qMarket = QMarket.market;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(qMarket.symbol.eq(market));

        List<Market> collect = jpaQueryFactory.selectFrom(qMarket)
                .where(condition)
                .stream()
                .toList();

        return collect.stream().findFirst().get();
    }

    public Market jpa(String market) {
        return marketJpaRepository.findBySymbol(market).stream().findFirst().get();
    }
}
