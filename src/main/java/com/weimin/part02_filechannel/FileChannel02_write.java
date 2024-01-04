package com.weimin.part02_filechannel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileChannel02_write {
    public static void main(String[] args) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/FileChannel02_write.txt");
             FileChannel to = fileOutputStream.getChannel()) {

            ByteBuffer buffer = UTF_8.encode("write");

            while (buffer.hasRemaining()) {
                int write = to.write(buffer);
                System.out.println("write bytes: " + write);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
