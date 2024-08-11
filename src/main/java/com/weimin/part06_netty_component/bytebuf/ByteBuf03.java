package com.weimin.part06_netty_component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.weimin.Logger.log;
// add VM options
// -Dio.netty.allocator.type=unpooled
public class ByteBuf03 {
    public static void main(String[] args) {
        // 默认初始容量 256
        ByteBuf buffer = ByteBufAllocator.DEFAULT.heapBuffer();

        // class io.netty.buffer.UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf
        // 非池化、堆内存
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
