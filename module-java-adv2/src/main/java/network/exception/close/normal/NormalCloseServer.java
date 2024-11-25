package network.exception.close.normal;

import util.MyLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.*;

public class NormalCloseServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(12345);
        Socket socket = serverSocket.accept();
        log("소캣 연결 : " + socket);

        Thread.sleep(1000);
        socket.close();
        serverSocket.close();
        log("소캣 종료");
    }
}
