package thread.control.yield;

import static util.ThreadUtils.sleep;

public class YieldMain {
    static final int THREAD_COUNT = 1000;

    public static void main(String[] args) {
        for (int i=1; i<=THREAD_COUNT; i++) {
            Thread thread = new Thread(new MyRunnable());
            thread.start();
        }
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + " - " + i);
//                sleep(1); // 스레드의 상태가 TIMED_WAITING 으로 변경된다. ( 실행 스케줄링에 제외 )
                // 스케줄링 큐에 들어가면서 다른 스레드에게 CPU 사용 기회를 양보한다.
                Thread.yield(); // 스레드의 상태는 계속 RUNNABLE 이다. ( 실행 스케줄링에 포함 )
            }
        }
    }
}
