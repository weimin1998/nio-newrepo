package com.weimin.part04_selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.weimin.part01_buffer.ByteBufferUtil.debugAll;

public class Selector01_Server_Handle_Accept_Read {
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

        source.compact();
    }

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);// 非阻塞
        serverSocketChannel.bind(new InetSocketAddress(9527));

        // 建立channel和selector的联系
        // 将channel注册到selector上，并且这个channel上，只关心accept事件
        // 当对应的事件发生时，使用SelectionKey来继续后面的操作
        SelectionKey ssckey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);


        while (true) {
            // 没有事件发生时，线程会阻塞
            // 有事件，线程才会恢复运行
            // 事件未处理时，select也不会阻塞
            // 事件发生后，要么处理，要么取消，不能置之不理，否则事件会一直存在，
            selector.select();

            // 处理事件
            // 得到所有SelectionKey的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);

                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    socketChannel.register(selector, SelectionKey.OP_READ, buffer);
                } else if (selectionKey.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        int read = channel.read(buffer);
                        if (read == -1) {
                            selectionKey.cancel();
                        } else {
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                System.out.println("test");
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                debugAll(newBuffer);
                                selectionKey.attach(newBuffer);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        selectionKey.cancel();
                    }
                }
            }
        }
    }
}
