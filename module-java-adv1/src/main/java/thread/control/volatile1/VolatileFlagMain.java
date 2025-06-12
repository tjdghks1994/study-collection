package thread.control.volatile1;

import util.MyLogger;
import util.ThreadUtils;

import static util.MyLogger.*;

public class VolatileFlagMain {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        log("runFlag = " + task.runFlag);
        thread.start();

        ThreadUtils.sleep(1000);
        log("runFlag를 false로 변경 시도");
        task.runFlag = false;   // 값을 변경하였지만 work 스레드는 종료되지 않음 ( 메모리 가시성 )
        log("runFlag = " + task.runFlag);
        log("main 종료");
    }

    static class MyTask implements Runnable {

//        boolean runFlag = true;
        volatile boolean runFlag = true; // cpu 코어의 캐시 메모리를 무시하고 메인 메모리를 접근하여 조회
        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
                // runFlag 가 false 로 변하면 탈출
            }
            log("task 종료");
        }
    }
}
