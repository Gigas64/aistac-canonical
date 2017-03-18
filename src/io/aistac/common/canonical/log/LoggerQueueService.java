/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * @(#)LoggerQueueService.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.log;

import static io.aistac.common.canonical.log.LoggerLevel.DEBUG;
import static io.aistac.common.canonical.log.LoggerLevel.ERROR;
import static io.aistac.common.canonical.log.LoggerLevel.FATAL;
import static io.aistac.common.canonical.log.LoggerLevel.INFO;
import static io.aistac.common.canonical.log.LoggerLevel.TRACE;
import static io.aistac.common.canonical.log.LoggerLevel.WARN;
import io.aistac.common.canonical.valueholder.ValueHolder;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The {@code LoggerQueueService} Class extends the methods of the parent class.
 *
 * @author Darryl Oatridge
 * @version 1.00 30-Mar-2016
 */
public class LoggerQueueService  {
    // Singleton Instance
    private volatile static LoggerQueueService INSTANCE;

    private final LinkedBlockingQueue<LoggerBean> queue;

    private final String queueOut = ValueHolder.uniqueName("LoggerQueueService", "queueOut");
    private final String name = "LoggerQueueService";
    private volatile LoggerLevel loglevel;

    // private Method to avoid instantiation externally
    private LoggerQueueService() {
        loglevel = ERROR;
        queue = new LinkedBlockingQueue<>();
    }

    /**
     * Singleton pattern to get the instance of the {@code LoggerQueueService} class
     * @return instance of the {@code LoggerQueueService}
     */
    @SuppressWarnings("DoubleCheckedLocking")
    public static LoggerQueueService getInstance() {
        if(INSTANCE == null) {
            synchronized (LoggerQueueService.class) {
                // Check again just incase before we synchronised an instance was created
                if(INSTANCE == null) {
                    INSTANCE = new LoggerQueueService();
                }
            }
        }
        return INSTANCE;
    }

    public LoggerBean take() throws InterruptedException {
        return queue.take();
    }

    public LoggerBean poll() {
        return queue.poll();
    }

    public LoggerBean poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    public void add(LoggerBean log) {
        // if we reach capacity then remove the oldest log
        while(!queue.offer(log)) {
            // just throw this away
            queue.remove();
        }
    }

    public void log(LoggerLevel level, String marker, String msg) {
        add(new LoggerBean(level.value(), marker, msg, name));
    }

    public void fatal(String marker, String msg) {
        if(LoggerLevel.isLogged(FATAL, loglevel)) {
            add(new LoggerBean(FATAL.value(), marker, msg, name));
        }
    }

    public void error(String marker, String msg) {
        if(LoggerLevel.isLogged(ERROR, loglevel)) {
            add(new LoggerBean(ERROR.value(), marker, msg, name));
        }
    }

    public void warn(String marker, String msg) {
        if(LoggerLevel.isLogged(WARN, loglevel)) {
            add(new LoggerBean(WARN.value(), marker, msg, name));
        }
    }

    public void info(String marker, String msg) {
        if(LoggerLevel.isLogged(INFO, loglevel)) {
            add(new LoggerBean(INFO.value(), marker, msg, name));
        }
    }

    public void debug(String marker, String msg) {
        if(LoggerLevel.isLogged(DEBUG, loglevel)) {
            add(new LoggerBean(DEBUG.value(), marker, msg, name));
        }
    }

    public void trace(String marker, String msg) {
        if(LoggerLevel.isLogged(TRACE, loglevel)) {
            add(new LoggerBean(TRACE.value(), marker, msg, name));
        }
    }

    public LoggerLevel getLogLevel() {
        return loglevel;
    }

    public void setLogLevel(LoggerLevel logLevel) {
        loglevel = logLevel;
    }

    public boolean hasRemaining() {
        return !queue.isEmpty();
    }

    public int pendingLogs() {
        return queue.size();
    }

    public void clearLogs() {
        queue.clear();
    }

    public boolean setLogLevel(String stringLevel) {
        switch(stringLevel) {
            case "FATAL":
                setLogLevel(FATAL);
                return true;
            case "ERROR":
                setLogLevel(ERROR);
                return true;
            case "WARN":
                setLogLevel(WARN);
                return true;
            case "INFO":
                setLogLevel(INFO);
                return true;
            case "DEBUG":
                setLogLevel(DEBUG);
                return true;
            case "TRACE":
                setLogLevel(TRACE);
                return true;
            default:
                return false;
        }
    }
 }
