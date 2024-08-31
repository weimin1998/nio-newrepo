package com.weimin.part10_youhua;

import com.weimin.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class TestConnectionTimeout {

    private static final Logger logger = new Logger(TestConnectionTimeout.class);

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    // 属于 SocketChannal 参数
                    // 用在客户端建立连接时，如果在指定毫秒内无法连接，会抛出 timeout 异常
                    // 实际上，超过2000多毫秒，就会报 Connection refused: no further information
                    // jdk17，不会timeout，直接报Connection refused: no further information
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2100)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler());
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080);
            future.sync().channel().closeFuture().sync(); // 断点1
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("timeout");
        } finally {
            group.shutdownGracefully();
        }
    }
}
