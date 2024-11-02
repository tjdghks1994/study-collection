package io.text;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReaderWriterMainV4 {
    private static final int BUFFER_SIZE = 8192;
    public static void main(String[] args) throws IOException {
        String writeString = "ABC\n가나다";
        System.out.println("== write string ==");
        System.out.println(writeString);

        // 파일에 쓰기
        FileWriter fw = new FileWriter(TextConst.FILE_NAME, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(fw, BUFFER_SIZE);
        bw.write(writeString);
        bw.close();

        // 파일 읽기
        StringBuilder sb = new StringBuilder();
        FileReader fr = new FileReader(TextConst.FILE_NAME, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(fr, BUFFER_SIZE);

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        System.out.println("== read string ==");
        System.out.println(sb);

    }
}
