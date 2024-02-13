package com.weimin.part06_netty_component.future;

import com.weimin.Logger;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class TestNettyFuture_sync_get {
    private static final Logger logger = new Logger(TestNettyFuture_sync_get.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        EventLoop eventLoop = group.next();

        // netty future
        Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                logger.debug("loading...");
                Thread.sleep(1000);
                return 100;
            }
        });

        logger.debug("waiting result");
        // 同步等待结果
        Integer integer = future.get();
        logger.debug(integer);

        group.shutdownGracefully();

    }
}
