package chat.client;

import util.MyLogger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static util.MyLogger.log;

public class WriteHandler implements Runnable {
    private static final String DELIMITER = "|";
    private final DataOutputStream output;
    private final Client client;
    private boolean closed = false;

    public WriteHandler(DataOutputStream output, Client client) {
        this.output = output;
        this.client = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            String username = inputUsername(scanner);
            output.writeUTF("/join" + DELIMITER + username);

            while (true) {
                String message = scanner.nextLine();    // 블로킹
                if (message.isEmpty()) {
                    continue;
                }

                if (message.equals("/exit")) {
                    output.writeUTF(message);
                    break;
                }

                if (message.startsWith("/")) {
                    output.writeUTF(message);
                } else {
                    output.writeUTF("/message" + DELIMITER + message);
                }
            }
        } catch (IOException | NoSuchElementException e) {
            log(e);
        } finally {
            client.close();
        }
    }

    public synchronized void close() {
        if (closed) {
            return;
        }

        try {
            System.in.close();  //  Scanner 입력 중지 ( 사용자의 입력을 닫음 )
        } catch (IOException e) {
            log(e);
        }
        closed = true;
        log("writeHandler 종료");
    }

    private static String inputUsername(Scanner scanner) {
        System.out.println("이름을 입력하세요.");
        String username;
        do {
            username = scanner.nextLine();
        } while (username.isEmpty());
        return username;
    }
}
