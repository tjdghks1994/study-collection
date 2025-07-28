package thread.executor.future;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CallableTestMain {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int value = 0;

        ExecutorService es = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            Future<Integer> future = es.submit(new MyCallable());
            value += future.get();
        }

        log("value = " + value);

        es.close();
    }

    static class MyCallable implements Callable<Integer> {

        @Override
        public Integer call() {
            return 100;
        }
    }
}
