package com.weimin.part08_sticky_bag_half_pack.linebasedFramedecoder;

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

import java.util.Random;

// 使用换行作为消息之间的边界
public class Client {
    static final Logger log = new Logger(Client.class);

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    log.debug("connected...");
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        // 会在连接channel建立后，触发active
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            log.debug("sending...");
                            ByteBuf buffer = ctx.alloc().buffer();

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("0123456789");
                            stringBuilder.append("\n");
                            stringBuilder.append("abcdefghigklmn");
                            stringBuilder.append("\n");
                            stringBuilder.append("opqrstuvwxyz");
                            stringBuilder.append("\n");
                            stringBuilder.append("hahahahaha");
                            stringBuilder.append("\n");
                            stringBuilder.append("hello");
                            stringBuilder.append("\n");
                            stringBuilder.append("byebye");
                            stringBuilder.append("\n");

                            buffer.writeBytes(stringBuilder.toString().getBytes());
                            ctx.writeAndFlush(buffer);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error(e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
