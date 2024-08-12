package com.weimin.part06_netty_component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;


// netty中零拷贝的体现，减少内存复制
public class ByteBuf05_slice_2 {
    public static void main(String[] args) {
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        System.out.println(ByteBufUtil.prettyHexDump(origin));

        ByteBuf b1 = origin.slice(0, 5);
        ByteBuf b2 = origin.slice(5, 5);

        // 切片后，切片后的bytebuf调用retain()，使得引用计数+1
        // 这样原始的bytebuf被释放后，也不影响切片的使用
        b1.retain();
        b2.retain();

        System.out.println(ByteBufUtil.prettyHexDump(b1));
        System.out.println(ByteBufUtil.prettyHexDump(b2));

        b1.setByte(0, 'b');
        System.out.println(ByteBufUtil.prettyHexDump(b1));
        System.out.println(ByteBufUtil.prettyHexDump(origin));


        // 原始的butebuf被释放了，那么切片也不能再使用了
        // 如何让原始的bytebuf被释放后，不影响切片的使用呢？
        //
        origin.release();

        System.out.println(ByteBufUtil.prettyHexDump(b1));
        System.out.println(ByteBufUtil.prettyHexDump(b2));
        b1.release();
        b2.release();

    }
}
