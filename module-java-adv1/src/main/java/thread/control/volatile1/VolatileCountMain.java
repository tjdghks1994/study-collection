package thread.control.volatile1;

import util.MyLogger;
import util.ThreadUtils;

import static util.MyLogger.*;

public class VolatileCountMain {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        ThreadUtils.sleep(1000);
        task.flag = false;
        log("flag = " + task.flag + ", count = " + task.count + " in main()");

    }

    static class MyTask implements Runnable {

//        boolean flag = true;
//        long count;

        volatile boolean flag = true;
        volatile long count;

        @Override
        public void run() {
            while (flag) {
                count++;
                // 1억번에 한번씩 출력
                if (count % 100_000_000 == 0) {
                    log("flag = " + flag + ", count = " + count + " in while()");
                }
            }
            log("flag = " + flag + ", count = " + count + " 종료!");
        }
    }
}
