package com.weimin;

import io.netty.buffer.ByteBuf;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class Logger {

    private final Class clazz;

    public Logger(Class clazz) {
        this.clazz = clazz;
    }

    public void info(Object msg) {
        System.out.println(now() + " INFO ---- [" + Thread.currentThread().getName() + "] " + clazz.getName() + " : " + msg);
    }

    public void error(Object msg) {
        System.out.println(now() + " ERROR ---- [" + Thread.currentThread().getName() + "] " + clazz.getName() + " : " + msg);
    }

    public void debug(Object msg) {
        System.out.println(now() + " DEBUG ---- [" + Thread.currentThread().getName() + "] " + clazz.getName() + " : " + msg);
    }
    private String now() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        return dateFormat.format(now);
    }

    public static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
