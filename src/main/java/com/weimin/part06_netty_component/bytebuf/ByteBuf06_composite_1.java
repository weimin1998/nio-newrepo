package com.weimin.part06_netty_component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;


// 两个bytebuf合并成一个
public class ByteBuf06_composite_1 {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer(5);
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});
        System.out.println(ByteBufUtil.prettyHexDump(buf1));
        System.out.println(ByteBufUtil.prettyHexDump(buf2));


        ByteBuf buf3 = ByteBufAllocator.DEFAULT
                .buffer(buf1.readableBytes() + buf2.readableBytes());
        buf3.writeBytes(buf1);
        buf3.writeBytes(buf2);
        System.out.println(ByteBufUtil.prettyHexDump(buf3));

        // 这种方法好不好？回答是不太好，因为进行了数据的内存复制操作

    }
}
