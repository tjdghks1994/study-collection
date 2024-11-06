package io.file;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class OldFileMain {
    public static void main(String[] args) throws IOException {
        File file = new File("module-java-adv2/temp/example.txt");
        File directory = new File("module-java-adv2/temp/exampleDir");

        System.out.println("File exists = " + file.exists());

        boolean created = file.createNewFile();
        System.out.println("File created = " + created);

        boolean dirCreated = directory.mkdir();
        System.out.println("Directory create = " + dirCreated);

//        boolean deleted = file.delete();
//        System.out.println("File deleted = " + deleted);

        System.out.println("Is file = " + file.isFile());
        System.out.println("Is directory = " + directory.isDirectory());
        System.out.println("File Name = " + file.getName());
        System.out.println("File Size = " + file.length() + " bytes");

        File newFile = new File("module-java-adv2/temp/newExample.txt");
        boolean renamed = file.renameTo(newFile);
        System.out.println("File renamed = " + renamed);

        long lastModified = newFile.lastModified();
        System.out.println("Last Modified = " + new Date(lastModified));
    }
}
