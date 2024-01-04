package com.weimin.part03_socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.weimin.part01_buffer.ByteBufferUtil.debugRead;

public class SocketChannel01_Block_Server {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(9527));

        ByteBuffer buffer = ByteBuffer.allocate(16);


        // 使用一个集合，存放所有的连接
        List<SocketChannel> channels = new ArrayList<>();

        while (true) {
            System.out.println("connecting...");
            SocketChannel socketChannel = serverSocketChannel.accept(); // 阻塞
            System.out.println("connected..." + socketChannel);
            channels.add(socketChannel);

            for (SocketChannel channel : channels) {
                System.out.println("before read.." + channel);
                channel.read(buffer);// 阻塞
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                System.out.println("after read.." + channel);
            }
        }
    }
}
