package com.weimin;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
