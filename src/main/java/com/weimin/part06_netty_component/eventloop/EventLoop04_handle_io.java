package com.weimin.part06_netty_component.eventloop;

import com.weimin.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class EventLoop04_handle_io {

    private static final Logger logger = new Logger(EventLoop04_handle_io.class);

    public static void main(String[] args) {
        // 分工细化2
        // 这个defaultEventLoopGroup 专门执行那些耗时长的handler
        EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
        EventLoopGroup boosEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(2);
        new ServerBootstrap()

                .group(boosEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                            @Override                                          //ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                logger.info(byteBuf.toString(Charset.defaultCharset()));

                                // 将消息传递给下一个handler
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(defaultEventLoopGroup, "handler2", new ChannelInboundHandlerAdapter() {
                            @Override                                          //ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                logger.info(byteBuf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
