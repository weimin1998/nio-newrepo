package com.weimin.part01_buffer;

import java.nio.ByteBuffer;

import static com.weimin.part01_buffer.ByteBufferUtil.debugAll;
import static java.nio.charset.StandardCharsets.UTF_8;

// 将string转为buffer
public class Buffer04_buffer_string {
    public static void main(String[] args) {
        //
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());
        debugAll(buffer);

        //
        ByteBuffer buffer1 = UTF_8.encode("hello");
        debugAll(buffer1);


        //
        ByteBuffer wrap = ByteBuffer.wrap("hello".getBytes());
        debugAll(wrap);

    }
}
