package network.tcp.autocloseable;

public class ResourceCloseMainV3 {
    public static void main(String[] args) {
        try {
            logic();
        } catch (CallException e) {
            System.out.println("CallException 예외 처리");
            throw new RuntimeException(e);
        }
    }

    private static void logic() throws CallException {
        ResourceV1 resource1 = null;
        ResourceV1 resource2 = null;

        try {
            resource1 = new ResourceV1("resource1");
            resource2 = new ResourceV1("resource2");

            resource1.call();
            resource2.callEx(); // 핵심 예외
        } catch (CallException e) {
            System.out.println("ex : " + e);
            throw e;
        } finally {
            System.out.println("자원 정리");
            if (resource2 != null) {
                try {
                    resource2.closeEx();    // CloseException 발생
                } catch (CloseException e) {
                    // close() 에서 발생한 예외는 버린다. 필요하면 로깅 정도만 남긴다.
                    System.out.println("close ex : " + e);
                }
            }
            if (resource1 != null) {
                try {
                    resource1.closeEx();
                } catch (CloseException e) {
                    // close() 에서 발생한 예외는 버린다. 필요하면 로깅 정도만 남긴다.
                    System.out.println("close ex : " + e);
                }
            }

        }

    }
}
