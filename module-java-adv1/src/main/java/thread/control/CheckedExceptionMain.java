package thread.control;

public class CheckedExceptionMain {
    public static void main(String[] args) {

    }

     static class CheckedRunnable implements Runnable {

        // 부모(Runnable)가 채크 예외를 던지지 않는 경우, 재정의된 자식 메서드도 체크 예외를 던질 수 없다.
         @Override
         public void run() /*throws Exception*/ {
//             throw new Exception();
         }
     }
}
