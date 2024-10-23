package io.start;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class StreamStartMain3 {
    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("module-java-adv2/temp/hello.dat");
        byte[] bytes = {65, 66, 67, 68};
        fos.write(bytes);
        fos.close();

        FileInputStream fis = new FileInputStream("module-java-adv2/temp/hello.dat");
        byte[] buffer = new byte[10];
        int readCount = fis.read(buffer, 0, 10);
        System.out.println("readCount = " + readCount);
        System.out.println(Arrays.toString(buffer));
        fis.close();
    }
}
