package com.weimin.part01_buffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.weimin.part01_buffer.ByteBufferUtil.debugAll;

// 分散读
public class Buffer05_fensandu {
    public static void main(String[] args) {

        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/Buffer05_fensandu.txt")) {

            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer buffer1 = ByteBuffer.allocate(3);
            ByteBuffer buffer2 = ByteBuffer.allocate(3);
            ByteBuffer buffer3 = ByteBuffer.allocate(5);
            long read = channel.read(new ByteBuffer[]{buffer1, buffer2, buffer3});
            System.out.println("read bytes: " + read);

            buffer1.flip();
            buffer2.flip();
            buffer3.flip();

            debugAll(buffer1);// own
            debugAll(buffer2);// two
            debugAll(buffer3);// three
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
