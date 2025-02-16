package com.example.stock.facade;

import com.example.stock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        // 락 획득에 실패한 경우 재시도를 기본으로 제공하기 때문에
        // 재시도 로직을 구현할 필요 없다
        RLock lock = redissonClient.getLock(id.toString()); // 락 획득

        try {
            // 몇 초 동안 락 획득을 시도할 것인지, 몇 초 동안 점유할 것인지 설정
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("LOCK 획득 실패");
                return;
            }

            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();  // 락 해제
        }
    }
}
