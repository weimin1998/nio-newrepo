package com.weimin.part06_netty_component.future;

import com.weimin.Logger;

import java.util.concurrent.*;

public class TestJDKFuture {

    private static final Logger logger = new Logger(TestJDKFuture.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                logger.debug("loading...");
                Thread.sleep(1000);
                return 100;
            }
        });

        // 主线程通过get获取结果；
        // 同步等待
        logger.debug("waiting result");
        Integer integer = future.get();
        logger.debug(integer);

        service.shutdown();

    }
}
