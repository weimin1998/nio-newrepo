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
import java.util.Scanner;

public class ChannelTest03_Client_close_sync {
    private static final Logger logger = new Logger(ChannelTest03_Client_close_sync.class);

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
        // 阻塞当前线程，直到channel真正被关闭。
        closeFuture.sync();
        logger.info("after close");


    }
}
