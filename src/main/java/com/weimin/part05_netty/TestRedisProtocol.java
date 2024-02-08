package com.weimin.part05_netty;

import com.weimin.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

public class TestRedisProtocol {
    private static final Logger logger = new Logger(TestRedisProtocol.class);

    public static void main(String[] args) {
        /**
         * redis在使用的时候，客户端在发送命令给redis服务器时，要遵循redis协议
         * 比如，客户端想要发送一条命令：set name zhangsan
         *
         * 但是发送给服务器的并不是简单的字符串，而是下面的格式：
         * 0.把命令当成一个数组，以这个命令为例，数组有三个元素；
         * 1.首先发送数组元素的个数：*3
         * 2.再发送每个元素的长度，以及元素本身，比如set的长度是3：
         *      $3
         *      set
         *      $4
         *      name
         *      $8
         *      zhangsan
         */

        final byte[] LINE = {13, 10};

        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LoggingHandler());
                    socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf byteBuf = ctx.alloc().buffer();

                            byteBuf.writeBytes("*3".getBytes());
                            byteBuf.writeBytes(LINE);
                            byteBuf.writeBytes("$3".getBytes());
                            byteBuf.writeBytes(LINE);
                            byteBuf.writeBytes("set".getBytes());
                            byteBuf.writeBytes(LINE);
                            byteBuf.writeBytes("$4".getBytes());
                            byteBuf.writeBytes(LINE);
                            byteBuf.writeBytes("name".getBytes());
                            byteBuf.writeBytes(LINE);
                            byteBuf.writeBytes("$8".getBytes());
                            byteBuf.writeBytes(LINE);
                            byteBuf.writeBytes("zhangsan".getBytes());
                            byteBuf.writeBytes(LINE);

                            ctx.writeAndFlush(byteBuf);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf byteBuf = (ByteBuf) msg;
                            logger.info(byteBuf.toString(Charset.defaultCharset()));// +OK
                        }
                    });

                }
            });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 6379).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            worker.shutdownGracefully();
        }
    }
}
