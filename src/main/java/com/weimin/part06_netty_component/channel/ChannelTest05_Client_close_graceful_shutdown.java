package com.weimin.part06_netty_component.channel;

import com.weimin.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class ChannelTest05_Client_close_graceful_shutdown {
    private static final Logger logger = new Logger(ChannelTest05_Client_close_graceful_shutdown.class);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("127.0.0.1", 8080));


        Channel channel = channelFuture.sync().channel();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    channel.close();
                    // do something
                    break;
                }

                channel.writeAndFlush(line);
            }
        }, "input").start();


        // 处理关闭
        ChannelFuture closeFuture = channel.closeFuture();
        logger.info("waiting close");

        // 将连接关闭后的善后工作交给nio线程
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.info("after close");
                // 优雅停止
                // 停止nioEventLoopGroup中的线程
                nioEventLoopGroup.shutdownGracefully();
            }
        });
    }
}
