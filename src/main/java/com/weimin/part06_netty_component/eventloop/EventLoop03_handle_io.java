package com.weimin.part06_netty_component.eventloop;

import com.weimin.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EventLoop03_handle_io {

    private static final Logger logger = new Logger(EventLoop03_handle_io.class);

    public static void main(String[] args) {
        EventLoopGroup boosEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(2);
        new ServerBootstrap()
                // 分工细化1
                // 第一个NioEventLoopGroup(boss) 只负责accept事件，并且使用到其中的一个线程
                // 第二个NioEventLoopGroup(worker) 负责读写事件，一个worker线程可以负责多个channel，一个channel会绑定一个worker。
                .group(boosEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override                                          //ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                // 指定字符集，最好显示的指定，不要使用默认的，万一客户端和服务器的字符集不一致，就会有问题
                                //logger.info(byteBuf.toString(Charset.defaultCharset()));
                                logger.info(byteBuf.toString(StandardCharsets.UTF_8));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
