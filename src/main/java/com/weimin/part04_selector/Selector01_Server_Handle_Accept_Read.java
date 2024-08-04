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

// 这个class是最终的版本，处理了所有的问题
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
            // 没有事件发生时，线程会阻塞，也就是select方法会在没有事件发生时阻塞线程
            // 有事件，线程才会恢复运行
            // 事件未处理时，select也不会阻塞
            // 事件发生后，要么处理，要么取消，不能置之不理，否则事件会一直存在，
            selector.select();

            // 当运行到select方法之后，说明有事件发生；

            // 处理事件
            // 得到所有SelectionKey的集合（事件集合）
            // 当某个channel上有事件发生时，就会有这么一个SelectionKey对象被创建，代表某个channel上发生了某一事件
            // selector管理多个channel，这里就拿到所有发生了事件的channel对应的SelectionKey
            // 然后遍历处理
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                // 区分事件类型
                if (selectionKey.isAcceptable()) {
                    // 这里的ServerSocketChannel 就是上面的 serverSocketChannel
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);

                    // 每个channel使用单独的buffer，这个buffer的生命周期和channel绑定
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    // socketChannel也注册到selector
                    socketChannel.register(selector, SelectionKey.OP_READ, buffer);
                    System.out.println("客户端请求连接建立：" + socketChannel);
                } else if (selectionKey.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        int read = channel.read(buffer);

                        // read等于-1，代表客户端正常断开
                        if (read == -1) {
                            System.out.println("客户端正常关闭连接："+selectionKey.channel());
                            selectionKey.cancel();
                        } else {
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                // 扩容
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                debugAll(newBuffer);
                                // 扩容后，再附到channel上
                                selectionKey.attach(newBuffer);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("客户端强制断开连接："+selectionKey.channel());
                        // 发生异常，代表客户端强制断开
                        selectionKey.cancel();
                    }
                }
            }
        }
    }
}
