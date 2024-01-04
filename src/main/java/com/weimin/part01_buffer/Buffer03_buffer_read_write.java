package com.weimin.part01_buffer;

import java.nio.ByteBuffer;

import static com.weimin.part01_buffer.ByteBufferUtil.debugAll;

public class Buffer03_buffer_read_write {
    public static void main(String[] args) {
        // 1.申请一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        debugAll(buffer);
        // 2.向buffer中写入数据
        buffer.put(new byte[]{'a','b','c','d'});
        // 3.查看buffer
        debugAll(buffer);
        // 4.切换到读模式
        buffer.flip();
        // 5.再次查看buffer，注意position和limit
        debugAll(buffer);
        // 6.从buffer中读数据
        buffer.get(new byte[4]);
        // 7.再次查看buffer，注意position和limit
        debugAll(buffer);
        // 8.如果想再次读取数据，调用rewind，将position重新置为0
        buffer.rewind();
        debugAll(buffer);
        // 9.只读一个数据
        System.out.println((char) buffer.get());
        // 10.切到写模式
        buffer.compact();
        // 11.因为第一个字节已经被读了，compact会将未被读的字节向前复制
        debugAll(buffer);
    }
}
