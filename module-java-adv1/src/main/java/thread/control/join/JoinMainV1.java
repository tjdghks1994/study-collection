package thread.control.join;

import util.MyLogger;
import util.ThreadUtils;

import static util.MyLogger.*;

public class JoinMainV1 {
    public static void main(String[] args) {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        thread1.start();
        thread2.start();

        log("task1.result = " + task1.result);
        log("task2.result = " + task2.result);

        int sumAll = task1.result + task2.result;
        log("task1 + task2 = " + sumAll);
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
