## 용어 정리

-----

### Pessimistic Lock (비관적 락)

- DB 수준에서의 락 설정 방법이다.
- `select .... for update` 구문을 활용해 레코드 수준의 락을 걸 수 있다.
- **충돌이 잦은 환경**에서는 낙관적 락 방식보다 더 나은 성능을 보인다.
- 데이터의 **정합성이 중요한 경우** 활용한다.
- 데드락이 발생할 수 있으므로 주의해야 한다.

### Optimistic Lock (낙관적 락)

- 애플리케이션 수준에서의 락 설정 방법이다.
- 낙관적 락을 활용하기 위해서는 `version` 컬럼을 별도로 추가해야 한다.
- 데이터 변경/삭제 시 **version 값이 일치**한 경우에만 작업이 성공한다. <br>
  ( where 조건절에 version 값을 활용 )
- 작업에 실패한 경우에는 **재시도를 하기 위한 로직을 별도로 작성**해주어야 한다.
- 충돌이 잦지 않은 환경에서 활용하기 좋은 방법이다.

### Named Lock (네임드 락)

- **지정된 문자열을 key 로 사용**하여 락을 설정하고 해제하는 방법이다.
- 락을 획득하고, 해제하는 로직을 직접 작성해주어야 한다.
- mysql 을 사용할 경우, 네임드 락을 위한 별도의 테이블을 구성하지 않아도 된다. <br>
  ( `select get_lock(key, timeout)` , `select release_lock(key)` )
- 동시성 이슈가 발생할 수 있는 비즈니스 로직은 락을 획득하고 해제하는 로직과 별도의 트랜잭션으로 실행시켜야 한다. <br>
  즉, 락을 획득하고 해제하는 로직의 트랜잭션과 별도로 실행하기 위해 비즈니스 로직에 `@Transactional(propagation= Propagation.REQUIRES_NEW)`를 활용한다.

### Redis Lettuce Lock

- 분산 환경에서의 락을 활용하는 방법이다.
- 네임드 락 방법과 유사하다.
- **특정 키에 대해 락을 설정**하고, 락이 걸린 키에 대해서는 다른 프로세스가 접근할 수 없다.
- Redis Lettuce Lock 을 획득하고 해제하는 예시 코드 
     ```java
    @Component
    public class RedisLockRepository {
    
        private RedisTemplate<String, String> redisTemplate;
    
        public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
            this.redisTemplate = redisTemplate;
        }
    
        public Boolean lock(Long key) {
            return redisTemplate
                    .opsForValue()
                    .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3000));
        }
    
        public Boolean unlock(Long key) {
            return redisTemplate.delete(generateKey(key));
        }
    
        private String generateKey(Long key) {
            return key.toString();
        }
    }
    ```
- 락 획득에 실패한 경우 재시도를 기본적으로 제공하지 않으므로 **재시도 로직을 직접 작성**해야 한다.
- `스핀 락` 방식을 사용하므로 <u>redis 에 부하가 발생할 수 있다는 것을 주의</u>해야 한다.

### Redis Redisson Lock

- 분산 환경에서의 락을 활용하는 방법이다.
- `spring-boot-starter-redis` 의 기본 락 라이브러리가 Lettuce 이므로 **Redisson 관련 라이브러리를 별도로 추가**해주어야 한다.
- 락 획득에 실패한 경우 재시도를 기본으로 제공해준다.
- Redis Redisson Lock 획득하고 해제하는 예시 코드
    ```java
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
    ```