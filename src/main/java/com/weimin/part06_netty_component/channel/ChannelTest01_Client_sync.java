package com.weimin.part06_netty_component.channel;

import com.weimin.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class ChannelTest01_Client_sync {
    private static final Logger logger = new Logger(ChannelTest01_Client_sync.class);
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

        // 1.使用sync方法同步等待连接建立完成
        channelFuture.sync();
        // 2.sync这个方法会阻塞住当前线程，直到nio线程连接建立完毕。
        Channel channel = channelFuture.channel();
        logger.info(channel);
        channel.writeAndFlush("hello");
    }
}
