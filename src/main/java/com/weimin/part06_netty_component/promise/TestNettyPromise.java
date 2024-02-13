package com.weimin.part06_netty_component.promise;

import com.weimin.Logger;
import com.weimin.part06_netty_component.future.TestNettyFuture_async_get;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;

public class TestNettyPromise {
    private static final Logger logger = new Logger(TestNettyPromise.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();

        // 可以主动创建promise，它是结果的容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        // 任意线程执行任务，向promise中填充结果
        new Thread(() -> {
            logger.debug("loading...");
            try {
                Thread.sleep(1000);
                promise.setSuccess(100);
            } catch (InterruptedException e) {
                promise.setFailure(e);
            }
        }).start();

        logger.debug("waiting...");
        Integer integer = promise.get();
        logger.debug(integer);

    }
}
