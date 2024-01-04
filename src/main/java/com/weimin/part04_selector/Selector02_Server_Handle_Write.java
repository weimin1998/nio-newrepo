package com.weimin.part04_selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Selector02_Server_Handle_Write {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);// 非阻塞
        serverSocketChannel.bind(new InetSocketAddress(9527));

        SelectionKey ssckey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);

                    SelectionKey sckey = socketChannel.register(selector, SelectionKey.OP_READ, null);


                    // 向客户端发送大量数据
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 300000000; i++) {
                        stringBuilder.append("a");
                    }

                    ByteBuffer buffer = Charset.defaultCharset().encode(stringBuilder.toString());

                    // 先写一次，看看写出去多少
                    int write = socketChannel.write(buffer);
                    System.out.println("第一次write: " + write);

                    // 如果还有没写完的
                    if (buffer.hasRemaining()) {
                        // 监听写事件
                        sckey.interestOps(sckey.interestOps() + SelectionKey.OP_WRITE);

                        // 将剩余没写完的数据attach到key上
                        sckey.attach(buffer);
                    }
                } else if (selectionKey.isWritable()) {
                    //
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                    SocketChannel channel = (SocketChannel) selectionKey.channel();

                    int write = channel.write(buffer);
                    System.out.println(write);

                    // 如果没有剩余，表示写完了
                    if (!buffer.hasRemaining()) {
                        // 则去掉attach的buffer，便于gc
                        selectionKey.attach(null);
                        // 也不需要再关注写事件
                        selectionKey.interestOps(selectionKey.interestOps() - SelectionKey.OP_WRITE);
                    }

                }
            }
        }
    }
}
