package com.weimin.part06_netty_component.pipeline_handler;

import com.weimin.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Server_02 {
    private static final Logger logger = new Logger(Server_02.class);

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                ChannelPipeline pipeline = nioSocketChannel.pipeline();

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

                                pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        logger.debug("4");
                                        super.write(ctx, msg, promise);
                                    }
                                });

                                pipeline.addLast("h3", new ChannelInboundHandlerAdapter() { // 自定义handler
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        logger.debug("3");
                                        super.channelRead(ctx, msg);
                                        // 向客户端写数据
                                        ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("hello".getBytes()));
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
