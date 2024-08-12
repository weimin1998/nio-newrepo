package com.weimin.part06_netty_component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;


// netty中零拷贝的体现，减少内存复制
public class ByteBuf05_slice_1 {
    public static void main(String[] args) {
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        System.out.println(ByteBufUtil.prettyHexDump(origin));

        // 切片，从第0个开始，取5个
        ByteBuf b1 = origin.slice(0, 5);
        // 切片，从第5个开始，取5个
        ByteBuf b2 = origin.slice(5, 5);

        System.out.println(ByteBufUtil.prettyHexDump(b1));
        System.out.println(ByteBufUtil.prettyHexDump(b2));

        // 将第一个切片的第一个字节修改为b，原始的bytebuf也被修改了
        // 因为本身就是使用同一块内存
        b1.setByte(0, 'b');
        System.out.println(ByteBufUtil.prettyHexDump(b1));
        System.out.println(ByteBufUtil.prettyHexDump(origin));

        // 切片后的bytebuf，最大容量被限制了，不允许再写入
        // Exception in thread "main" java.lang.IndexOutOfBoundsException
        // b1.writeByte('x');


        // 原始的butebuf被释放了，那么切片也不能再使用了
        origin.release();
        // Exception in thread "main" io.netty.util.IllegalReferenceCountException: refCnt: 0
        System.out.println(ByteBufUtil.prettyHexDump(b1));
        System.out.println(ByteBufUtil.prettyHexDump(b2));

    }
}
