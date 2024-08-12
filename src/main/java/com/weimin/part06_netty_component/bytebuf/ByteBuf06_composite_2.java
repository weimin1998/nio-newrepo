package com.weimin.part06_netty_component.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;


// 两个bytebuf合并成一个
public class ByteBuf06_composite_2 {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer(5);
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});
        System.out.println(ByteBufUtil.prettyHexDump(buf1));
        System.out.println(ByteBufUtil.prettyHexDump(buf2));


        CompositeByteBuf buf3 = ByteBufAllocator.DEFAULT.compositeBuffer();
        // true 表示增加新的 ByteBuf 自动递增 write index, 否则 write index 会始终为 0
        buf3.addComponents(true, buf1, buf2);
        System.out.println(ByteBufUtil.prettyHexDump(buf3));

        // CompositeByteBuf 是一个组合的 ByteBuf，它内部维护了一个 Component 数组，
        // 每个 Component 管理一个 ByteBuf，记录了这个 ByteBuf 相对于整体偏移量等信息，代表着整体中某一段的数据。
        //
        //优点，对外是一个虚拟视图，组合这些 ByteBuf 不会产生内存复制
        //缺点，复杂了很多，多次操作会带来性能的损耗

    }
}
