package com.weimin.part02_filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

// file channel，复制内容到另一个文件
public class FileChannel01_transfor {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/FileChannel01_from_txt");
             FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/FileChannel01_to_txt");
             FileChannel from = fileInputStream.getChannel();
             FileChannel to = fileOutputStream.getChannel()) {

            from.transferTo(0, from.size(), to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
