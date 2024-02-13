package com.weimin.part06_netty_component.pipeline_handler;

import com.weimin.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Server_01 {
    private static final Logger logger = new Logger(Server_01.class);

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                ChannelPipeline pipeline = nioSocketChannel.pipeline();

                                // 服务器接收客户端的数据，是入站
                                // 服务器向客户端发送数据，是出站
                                // 添加处理器
                                // head - h1 - h2 - h3 - h4 - h5 - h6 - tail
                                // 这是双向链表
                                // 入站处理器是顺序执行
                                // 出站处理器是倒序执行
                                pipeline.addLast("h1", new ChannelInboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        logger.debug("1");
                                        super.channelRead(ctx, msg);
                                    }
                                });

                                pipeline.addLast("h2", new ChannelInboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        logger.debug("2");
                                        super.channelRead(ctx, msg);
                                    }
                                });

                                pipeline.addLast("h3", new ChannelInboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        logger.debug("3");
                                        super.channelRead(ctx, msg);
                                        // 向客户端写数据
                                        nioSocketChannel.writeAndFlush(ctx.alloc().buffer().writeBytes("hello".getBytes()));
                                    }
                                });

                                pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        logger.debug("4");
                                        super.write(ctx, msg, promise);
                                    }
                                });

                                pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        logger.debug("5");
                                        super.write(ctx, msg, promise);
                                    }
                                });

                                pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        logger.debug("6");
                                        super.write(ctx, msg, promise);
                                    }
                                });
                            }
                        })
                .bind(9527);
    }
}
