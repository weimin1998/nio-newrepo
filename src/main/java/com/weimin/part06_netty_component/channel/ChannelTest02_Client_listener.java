package com.weimin.part06_netty_component.channel;

import com.weimin.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class ChannelTest02_Client_listener {
    private static final Logger logger = new Logger(ChannelTest02_Client_listener.class);

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                // 连接到服务器
                // connect是异步非阻塞的，main线程调用的connect方法，真正执行connect操作的是nio线程
                .connect(new InetSocketAddress("127.0.0.1", 8080));


        // 异步处理结果
        // 这个方法将 '获取channel并且使用channel发送数据的工作' 交给nio线程来做，不是main线程来做了。
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 当连接真正建立好后，调用下面的方法
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                logger.info(channel);
                channel.writeAndFlush("hello");
            }
        });
    }
}
