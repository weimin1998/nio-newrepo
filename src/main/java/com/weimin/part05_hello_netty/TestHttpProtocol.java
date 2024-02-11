package com.weimin.part05_hello_netty;

import com.weimin.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

public class TestHttpProtocol {
    private static final Logger logger = new Logger(TestHttpProtocol.class);

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
                    socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    socketChannel.pipeline().addLast(new HttpServerCodec());
//                    socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
//                        @Override
//                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                            logger.info(msg.toString());
//                            logger.info(msg.getClass().toString());
//
//                            if(msg instanceof HttpRequest){
//                                // 处理请求头和请求行
//                            }else if(msg instanceof HttpContent){
//                                // 处理请求体
//                            }
//                        }
//                    });

                    // SimpleChannelInboundHandler
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
