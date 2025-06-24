package thread.sync.lock;

import util.MyLogger;
import util.ThreadUtils;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.*;
import static util.ThreadUtils.*;

public class LockSupportMainV1 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ParkTest(), "Thread-1");
        t1.start();

        // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다
        sleep(100);
        log("Thread-1 state : " + t1.getState());

        log("main -> unpark(Thread-1)");
        LockSupport.unpark(t1); // unpark 사용 Waiting (Timed-Waiting) -> Runnable
//        t1.interrupt(); // interrupt 사용 Waiting (Timed-Waiting) -> Runnable
        log("main -> park(Thread-1)");
    }

    static class ParkTest implements Runnable {
        @Override
        public void run() {
            log("park 시작");
            LockSupport.park(); // Runnable -> Waiting
            log("park 종료 state : " + Thread.currentThread().getState());
            log("인터럽트 상태 : " + Thread.currentThread().isInterrupted());
        }
    }
}
