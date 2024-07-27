package com.weimin.part01_buffer;

import java.nio.ByteBuffer;

import static com.weimin.part01_buffer.ByteBufferUtil.debugAll;

// 粘包半包

/**
 * 客户端向服务器原始数据：
 * Hello, world\n
 * I'm weimin\n
 * How are you?\n
 * 三句话，使用\n分隔
 * <p>
 * 但是服务器端接收的时候，把这三句话
 * 变成了下面的两个bytebuffer
 * Hello, world\nI'm weimin\nHo
 * w are you?\n
 * <p>
 *
 * 这种现实在网络中称为粘包半包现象
 * 出现粘包是客户端为了效率考虑，将数据一次性发给服务器，比分三次发送效率高；
 * 出现半包是因为服务器端缓冲区的大小限制，buffer不可能无限大，可能只能接收第一条消息加第二条消息加第三条消息的一半，
 * 第三条消息的另一半，只能 等缓冲区有空余了。或者使用另外的buffer接收；
 * <p>
 *
 * 如何将内容复原？
 * <p>
 * 但是实际上处理粘包半包问题，分隔符的方式并不是很推荐，了解即可。
 */
public class Buffer07_Sticky_half_pack {
    public static void main(String[] args) {

        ByteBuffer source = ByteBuffer.allocate(32);
        // 第一次接收到第一条消息以及第二条消息加第三条消息的一半
        source.put("Hello, world\nI'm weimin\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);

    }

    private static void split(ByteBuffer source) {
        // 先切换读模式；
        source.flip();

        // 那么从0到limit的位置，就是可读的数据（忘记了的话，看buffer图解笔记）
        for (int i = 0; i < source.limit(); i++) {
            byte b = source.get(i);// get(i)不会使指针移动，仅仅是获取数组对应索引位置的数据；

            // 一旦读到的数据是 \n ，说明读到了一条完整的消息
            if (b == '\n') {
                // 然后就可以把这条完整的数据，存到新的buffer里；
                // 新的buffer应该分配多大呢？应该是消息实际的长度
                // 那么消息的长度是多少呢？换行符的索引 + 1 - 消息的起始索引
                int length = i + 1 - source.position();

                // 计算好消息的长度后，创建对应长度的buffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 然后把消息存到buffer中
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }

        }
        // 写模式，注意不要用clear()，因为可能还有数据没读完；（忘记了的话，看buffer图解笔记）
        source.compact();
    }
}
