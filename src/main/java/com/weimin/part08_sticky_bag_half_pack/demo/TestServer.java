package com.weimin.part08_sticky_bag_half_pack.demo;

import com.weimin.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class TestServer {

    private static final Logger logger = new Logger(TestServer.class);

    public static void main(String[] args) {
        EventLoopGroup boosEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(2);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_RCVBUF, 9)// 修改客户端缓冲区
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
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
