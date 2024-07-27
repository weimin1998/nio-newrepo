package com.weimin.part01_buffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Buffer01_HelloWorld {
    public static void main(String[] args) {
        try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/Buffer01_HelloWorld.txt")) {
            // 获取channel
            FileChannel channel = fileInputStream.getChannel();

            // 准备一个buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);

            int read;

            // 从channel中读取数据，并写到buffer中
            while ((read = channel.read(byteBuffer)) != -1) {
                System.out.println("读到的字节数：" + read);
                // flip，开启读模式
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    System.out.println((char) b);
                }
                byteBuffer.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
