package com.weimin.part06_netty_component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.weimin.Logger.log;

public class ByteBuf02 {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        log(buffer);
        // 大端写入
        // buffer.writeInt(5);

        // 小端写入
        buffer.writeIntLE(5);

        log(buffer);
    }
}
