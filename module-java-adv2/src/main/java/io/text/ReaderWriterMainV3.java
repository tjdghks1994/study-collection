package io.text;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReaderWriterMainV3 {
    public static void main(String[] args) throws IOException {
        String writeString = "ABC";
        System.out.println("write String : " + writeString);

        // 파일에 쓰기
        FileWriter fw = new FileWriter(TextConst.FILE_NAME, StandardCharsets.UTF_8);
        fw.write(writeString);
        fw.close();

        // 파일 읽기
        StringBuilder sb = new StringBuilder();
        FileReader fr = new FileReader(TextConst.FILE_NAME, StandardCharsets.UTF_8);
        int ch;
        while ((ch = fr.read()) != -1) {
            sb.append((char) ch);
        }
        fr.close();

        System.out.println("read String : " + sb);

    }
}
