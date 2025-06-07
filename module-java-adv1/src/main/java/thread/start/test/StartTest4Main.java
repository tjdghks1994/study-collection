package thread.start.test;

import util.MyLogger;

import static util.MyLogger.*;

public class StartTest4Main {
    public static void main(String[] args) {
        PrintWork A = new PrintWork("A", 1000);
        PrintWork B = new PrintWork("B", 500);
        Thread threadA = new Thread(A, "Thread-A");
        Thread threadB = new Thread(B, "Thread-B");
        threadA.start();
        threadB.start();
    }

    static class PrintWork  implements Runnable {
        private String content;
        private int sleepMs;

        public PrintWork(String content, int sleepMs) {
            this.content = content;
            this.sleepMs = sleepMs;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log(content);
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
