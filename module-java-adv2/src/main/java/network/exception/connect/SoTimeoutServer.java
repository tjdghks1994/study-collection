package network.exception.connect;

import java.io.IOException;
import java.net.ServerSocket;

public class SoTimeoutServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(12345);
        serverSocket.accept();
        // 서버가 응답을 하지 않기 위해
        Thread.sleep(10000000);
    }
}
