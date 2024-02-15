package com.weimin.part09_protocol;

import com.weimin.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

public class TestHttpProtocol_handle_header {
    private static final Logger logger = new Logger(TestHttpProtocol_handle_header.class);

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    socketChannel.pipeline().addLast(new HttpServerCodec()); // http协议的编解码器

                    // SimpleChannelInboundHandler，只关心处理请求头和请求行
                    socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
                            logger.info(httpRequest.uri());

                            DefaultFullHttpResponse response =
                                    new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);

                            byte[] msg = "<h1>Hello, I'm server!</h1>".getBytes();
                            response.content().writeBytes(msg);
                            // 告诉浏览器响应数据的长度，防止浏览器一直等待
                            response.headers().setInt(CONTENT_LENGTH, msg.length);

                            channelHandlerContext.writeAndFlush(response);
                        }
                    });
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
