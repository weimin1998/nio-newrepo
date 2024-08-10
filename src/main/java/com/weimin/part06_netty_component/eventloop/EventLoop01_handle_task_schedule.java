package com.weimin.part06_netty_component.eventloop;

import com.weimin.Logger;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;

import java.util.concurrent.TimeUnit;

// EventLoop 相当于前面的worker
// EventLoop,普通任务，定时任务
public class EventLoop01_handle_task_schedule {
    private static final Logger logger = new Logger(EventLoop01_handle_task_schedule.class);

    public static void main(String[] args) {
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);// 可以处理io事件，普通任务，定时任务
        //EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();// 可以处理普通任务，定时任务

        // 获取当前计算机的cpu核心数
        System.out.println(NettyRuntime.availableProcessors());

        // 获取下一个eventloop，当前eventLoopGroup中，只有两个
        System.out.println(nioEventLoopGroup.next());//获取第一个
        System.out.println(nioEventLoopGroup.next());//获取第二个
        System.out.println(nioEventLoopGroup.next());//获取第一个
        System.out.println(nioEventLoopGroup.next());//获取第二个

        // 执行普通任务
        nioEventLoopGroup.next().submit(() -> logger.info("OK"));
        // 执行定时任务
        nioEventLoopGroup.next().scheduleAtFixedRate(() -> logger.info("ok"), 0, 1, TimeUnit.SECONDS);
    }
}
