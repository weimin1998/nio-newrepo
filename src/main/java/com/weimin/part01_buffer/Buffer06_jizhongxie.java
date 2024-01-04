package com.weimin.part01_buffer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

// 集中写
public class Buffer06_jizhongxie {
    public static void main(String[] args) {
        ByteBuffer buffer1 = UTF_8.encode("hello");
        ByteBuffer buffer2 = UTF_8.encode("world");
        ByteBuffer buffer3 = UTF_8.encode("你好");

        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/Buffer06_jizhongxie.txt")) {
            FileChannel channel = fileOutputStream.getChannel();

            long write = channel.write(new ByteBuffer[]{buffer1, buffer2, buffer3});
            System.out.println("write bytes: " + write);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
