package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

public class BoundedQueueV6 implements BoundedQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition consumerCondition = lock.newCondition();    // 소비자 스레드 대기 집합(공간)
    private final Condition producerCondition = lock.newCondition();    // 생산자 스레드 대기 집합(공간)

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV6(int max) {
        this.max = max;
    }

    @Override
    public void put(String data) {
        lock.lock();

        try {
            while (queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기 : " + data);
                try {
                    producerCondition.await();
                    log("[put] 생산자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            queue.offer(data);
            log("[put] 생산자 데이터 저장, consumerCondition.signal() 호출");
            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String take() {
        lock.lock();

        try {
            while (queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                try {
                    consumerCondition.await();
                    log("[take] 소비자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String data = queue.poll();
            log("[take] 소비자 데이터 획득, producerCondition.signal() 호출");
            producerCondition.signal();

            return data;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
