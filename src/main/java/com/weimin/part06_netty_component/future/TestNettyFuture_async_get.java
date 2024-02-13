package com.weimin.part06_netty_component.future;

import com.weimin.Logger;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class TestNettyFuture_async_get {
    private static final Logger logger = new Logger(TestNettyFuture_async_get.class);

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

        // 异步等待结果
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                logger.debug("waiting result");

                // 此时一定有结果
                // 并且是执行任务的线程自己获取结果
                Object now = future.getNow();
                logger.debug(now);

            }
        });

        group.shutdownGracefully();

    }
}
