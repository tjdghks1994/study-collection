package thread.sync.lock;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class LockSupportMainV2 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ParkTest(), "Thread-1");
        t1.start();

        // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다
        sleep(100);
        log("Thread-1 state : " + t1.getState());
    }

    static class ParkTest implements Runnable {
        @Override
        public void run() {
            log("parkNanos 시작");
            LockSupport.parkNanos(2000_000000); // Runnable -> Timed-Waiting
            log("parkNanos 종료 state : " + Thread.currentThread().getState());
            log("인터럽트 상태 : " + Thread.currentThread().isInterrupted());
        }
    }
}
