package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;

public class SumTaskMainV2_Bad {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SumTask2 task1 = new SumTask2(1, 50);
        SumTask2 task2 = new SumTask2(51, 100);

        ExecutorService es = Executors.newFixedThreadPool(2);

        Future<Integer> future = es.submit(task1);  // non-blocking
        Integer sum1 = future.get();    // blocking

        Future<Integer> future2 = es.submit(task2); // non-blocking
        Integer sum2 = future2.get();   // blocking

        log("task1.result = " + sum1);
        log("task2.result = " + sum2);

        int sumAll = sum1 + sum2;
        log("task1 + task2 = " + sumAll);
        log("End");

        es.close();
    }

    static class SumTask2 implements Callable<Integer> {

        int startValue;
        int endValue;

        public SumTask2(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public Integer call() throws InterruptedException {
            log("작업 시작");
            Thread.sleep(2000);
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            log("작업 완료 result = " + sum);

            return sum;
        }
    }
}
