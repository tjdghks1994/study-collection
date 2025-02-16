package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {
    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        // 락 획득에 실패한 경우 재시도를 기본으로 제공하지 않으므로
        // 직접 재시도 하기 위함
        while (!redisLockRepository.lock(id)) { // 락 획득
            Thread.sleep(100);  // redis 부하를 줄이기 위해 스레드 대기시간 설정
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id); // 락 해제
        }
    }
}
