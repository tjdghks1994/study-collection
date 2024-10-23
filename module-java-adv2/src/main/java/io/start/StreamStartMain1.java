package io.start;

import java.io.*;

public class StreamStartMain1 {
    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("module-java-adv2/temp/hello.dat");
            fos.write(65);
            fos.write(66);
            fos.write(67);
            fos.close();

            FileInputStream fis = new FileInputStream("module-java-adv2/temp/hello.dat");
            System.out.println(fis.read());
            System.out.println(fis.read());
            System.out.println(fis.read());
            System.out.println(fis.read()); // 파일의 끝에 도달해서 더는 읽을 내용이 없으면 -1을 반환 (EOF - End Of File)
            fis.close();    // 외부 자원은 사용 후 반드시 닫아야 함
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
