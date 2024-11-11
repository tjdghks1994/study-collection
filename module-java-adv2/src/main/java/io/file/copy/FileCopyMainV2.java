package io.file.copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopyMainV2 {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream("module-java-adv2/temp/copy.dat");
        FileOutputStream fos = new FileOutputStream("module-java-adv2/temp/copy_new.dat");
        // inputStream 을 읽어서 outputStream 에 내보낸다
        fis.transferTo(fos);

        fis.close();
        fos.close();

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken : " + (endTime - startTime) + "ms");
    }
}
