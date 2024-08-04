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

// 这个class演示，消息边界问题
public class Selector01_Server_Msg_Bound_Issue {
    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
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
                    socketChannel.register(selector, SelectionKey.OP_READ, null);
                    System.out.println("客户端请求连接建立：" + socketChannel);
                } else if (selectionKey.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(4);
                        int read = channel.read(buffer);
                        if (read == -1) {
                            System.out.println("客户端正常关闭连接："+selectionKey.channel());
                            selectionKey.cancel();
                        } else {
                            // 在这里演示消息边界问题
                            buffer.flip();
                            System.out.println(Charset.defaultCharset().decode(buffer));

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("客户端强制断开连接："+selectionKey.channel());
                        selectionKey.cancel();
                    }
                }
            }
        }
    }
}
