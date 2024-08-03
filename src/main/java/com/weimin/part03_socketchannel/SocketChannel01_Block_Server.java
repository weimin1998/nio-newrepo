package com.weimin.part03_socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.weimin.part01_buffer.ByteBufferUtil.debugRead;

// 服务器单线程处理请求
public class SocketChannel01_Block_Server {
    public static void main(String[] args) throws IOException {

        // 创建服务器对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(9527));

        // 服务器缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);


        // 使用一个集合，存放所有的连接
        List<SocketChannel> channels = new ArrayList<>();

        // 服务器要一直保持运行，不停接收客户端的连接
        while (true) {
            System.out.println("connecting...");
            // 建立与客户端的连接
            SocketChannel socketChannel = serverSocketChannel.accept(); // 阻塞，线程停止运行
            System.out.println("connected..." + socketChannel);
            // 将这些连接放到集合里，统一操作
            channels.add(socketChannel);

            for (SocketChannel channel : channels) {
                System.out.println("before read.." + channel);
                // 读取客户端发送的数据
                channel.read(buffer);// 阻塞，线程停止运行
                buffer.flip();
                debugRead(buffer);
                // 每处理完一个连接，清空buffer，以便处理下个连接的数据
                buffer.clear();
                System.out.println("after read.." + channel);
            }
        }
    }
}
