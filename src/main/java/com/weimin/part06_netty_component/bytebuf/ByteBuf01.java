package com.weimin.part06_netty_component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.weimin.Logger.log;

public class ByteBuf01 {
    public static void main(String[] args) {
        // 默认初始容量 256
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        // class io.netty.buffer.PooledUnsafeDirectByteBuf
        // 默认是 池化、直接内存
        System.out.println(buffer.getClass());

        log(buffer);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 300; i++) {
            stringBuilder.append("a");
        }

        // 可以动态扩容
        buffer.writeBytes(stringBuilder.toString().getBytes());

        log(buffer);
    }
}
