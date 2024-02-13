package com.weimin.part06_netty_component.pipeline_handler;

import com.weimin.Logger;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;

public class TestEmbeddedChannel {
    private static final Logger logger = new Logger(TestEmbeddedChannel.class);

    public static void main(String[] args) {

        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() { // 自定义handler
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.debug("1");
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter() { // 自定义handler
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.debug("2");
                super.channelRead(ctx, msg);
            }
        };

        ChannelOutboundHandlerAdapter h3 = new ChannelOutboundHandlerAdapter() { // 自定义handler
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.debug("3");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter() { // 自定义handler
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.debug("4");
                super.write(ctx, msg, promise);
            }
        };

        // EmbeddedChannel 调试工具
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(h1, h2, h3, h4);

        // 测试入站处理器
        embeddedChannel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
        // 测试出站处理器
        embeddedChannel.writeOutbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
    }
}
