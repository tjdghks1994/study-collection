package network.exception.connect;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectTimeoutMain2 {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        try {
            // TCP 연결을 바로 하지 않는다
            Socket socket = new Socket();
            // 타임아웃 설정을 지정하고 TCP 연결
            socket.connect(new InetSocketAddress("192.168.1.250", 45678), 3000);
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("end = " + (end - start) + "ms");
    }
}
