package com.weimin.part01_buffer;

import java.nio.ByteBuffer;

import static com.weimin.part01_buffer.ByteBufferUtil.debugAll;

// 粘包半包

/**
 * 原始数据：
 * Hello, world\n
 * I'm weimin\n
 * How are you?\n
 *
 * 变成了下面的两个bytebuffer
 * Hello, world\nI'm weimin\nHo
 * w are you?\n
 */
public class Buffer07_Sticky_half_pack {
    public static void main(String[] args) {

        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello, world\nI'm weimin\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);

    }

    private static void split(ByteBuffer source) {
        // 先切换读模式；
        source.flip();

        for (int i = 0; i < source.limit(); i++) {
            byte b = source.get(i);
            if (b == '\n') {
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);

                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }

        }
        // 写模式
        source.compact();
    }
}
