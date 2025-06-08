package thread.control.join;

import util.ThreadUtils;

import static util.MyLogger.log;

public class JoinMainV4 {
    public static void main(String[] args) throws InterruptedException {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        Thread thread1 = new Thread(task1, "thread-1");

        thread1.start();

        // 스레드가 종료될 때 까지 대기
        log("join(1000) - main 스레드가 thread1 작업 1초 대기");
        thread1.join(1000);
        log("main 스레드 대기 완료");

        log("task1.result = " + task1.result);
        log("End");
    }

     static class SumTask implements Runnable {

        int startValue;
        int endValue;
        int result;

         public SumTask(int startValue, int endValue) {
             this.startValue = startValue;
             this.endValue = endValue;
         }

         @Override
         public void run() {
             log("작업 시작");
             ThreadUtils.sleep(2000);
             int sum = 0;
             for (int i=startValue;i<=endValue;i++) {
                 sum += i;
             }
             result = sum;
             log("작업 완료 result = " + result);
         }
     }
}
