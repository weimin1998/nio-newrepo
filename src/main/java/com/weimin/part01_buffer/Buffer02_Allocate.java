package com.weimin.part01_buffer;

import java.nio.ByteBuffer;

public class Buffer02_Allocate {

    public static void main(String[] args) {

        // 从堆内存中分配
        ByteBuffer allocate = ByteBuffer.allocate(16);

        // 从直接内存中分配（物理内存）
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16);

        System.out.println(allocate.getClass()); // class java.nio.HeapByteBuffer
        System.out.println(allocateDirect.getClass()); // class java.nio.DirectByteBuffer

    }
}
