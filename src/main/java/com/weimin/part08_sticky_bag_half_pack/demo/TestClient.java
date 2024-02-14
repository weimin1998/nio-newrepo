package com.weimin.part08_sticky_bag_half_pack.demo;

import com.weimin.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class TestClient {
    private static final Logger logger = new Logger(TestClient.class);

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
                .connect(new InetSocketAddress("127.0.0.1", 8080));
        ChannelFuture future = channelFuture.sync();
        Channel channel = future.channel();
        logger.info(channel.toString());

        for (int i = 0; i < 20; i++) {
            channel.writeAndFlush("1234567890");
        }


    }
}
