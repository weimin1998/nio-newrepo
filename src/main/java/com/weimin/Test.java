package com.weimin;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

public class Test {
    private static final Logger logger = new Logger(Test.class);
    public static void main(String[] args) {
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(5);

        nioEventLoopGroup.next().scheduleAtFixedRate(() -> logger.info("ok"), 0, 1, TimeUnit.SECONDS);
        nioEventLoopGroup.next().scheduleAtFixedRate(() -> logger.info("ok"), 0, 1, TimeUnit.SECONDS);
        nioEventLoopGroup.next().scheduleAtFixedRate(() -> logger.info("ok"), 0, 1, TimeUnit.SECONDS);
        nioEventLoopGroup.next().scheduleAtFixedRate(() -> logger.info("ok"), 0, 1, TimeUnit.SECONDS);
        nioEventLoopGroup.next().scheduleAtFixedRate(() -> logger.info("ok"), 0, 1, TimeUnit.SECONDS);

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // main线程执行完，不会影响其余的非守护线程
        // 进程停止，则所有的线程停止
    }
}
