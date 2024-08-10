package com.weimin.part06_netty_component.eventloop;

import com.weimin.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class EventLoopTestClient {
    private static final Logger logger = new Logger(EventLoopTestClient.class);
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
                // connect是异步非阻塞的，main线程调用的connect，真正执行connect的是nio线程
                .connect(new InetSocketAddress("127.0.0.1", 8080));
        ChannelFuture future = channelFuture.sync();
        Channel channel = future.channel();
        logger.info(channel.toString());
        System.out.println("break");// 断点打在这里，然后手动发数据，断点类型注意为Thread
//        channel.writeAndFlush("hello");
//        channel.writeAndFlush("world");
//        channel.writeAndFlush("I'm client!");


    }
}
