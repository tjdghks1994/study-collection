package io.file.copy;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreateCopyFile {

    private static final int FILE_SIZE = 200 * 1024 * 1024; // 200mb

    public static void main(String[] args) throws IOException {
        String filename = "module-java-adv2/temp/copy.dat";
        long startTime = System.currentTimeMillis();
        FileOutputStream fos = new FileOutputStream(filename);
        byte[] buffer = new byte[FILE_SIZE];
        fos.write(buffer);
        fos.close();

        long endTime = System.currentTimeMillis();
        System.out.println("File create : " + filename);
        System.out.println("File size : " + FILE_SIZE / 1024 / 1024 + "MB");
        System.out.println("Time taken : " + (endTime - startTime) + "ms");
    }
}
