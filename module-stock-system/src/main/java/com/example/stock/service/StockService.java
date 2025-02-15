package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // Named Lock 을 사용할 때, 부모 트랜잭션과 별도의 트랜잭션으로 실행시키는 이유는
    // Named Lock 을 획득하고 해제하는 트랜잭션과 동일한 트랜잭션이라면
    // 재고 감소를 실행하고 실제 DB commit 이 반영되기 전 락이 풀려
    // 락이 풀려있고 DB commit 이 반영되기 전인 사이 시간에 다른 요청이 들어오게 된다면
    // 동시성 이슈가 여전히 발생하기 때문이다 ( synchronized 와 동일한 문제 )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long id, Long quantity) {
        // Stock 조회
        Stock stock = stockRepository.findById(id).orElseThrow();
        // 재고를 감소시킨 뒤
        stock.decrease(quantity);
        // 갱신된 값을 저장
        stockRepository.saveAndFlush(stock);
    }
}
