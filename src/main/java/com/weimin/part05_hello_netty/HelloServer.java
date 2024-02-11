package com.weimin.part05_hello_netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        // 1。启动类，负责组装netty组件，启动服务器
        new ServerBootstrap()
                // selector thread组
                .group(new NioEventLoopGroup())
                // 选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                // boss负责处理连接，worker负责处理读写，决定了worker能执行哪些操作(handler)
                .childHandler(
                        // channel 代表和客户端数据读写的通道，Initializer，负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringDecoder()); // 将byteBuf转为字符串
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() { // 自定义handler
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(9527);
    }
}
