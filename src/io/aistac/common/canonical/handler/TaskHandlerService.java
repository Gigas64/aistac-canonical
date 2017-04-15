 /*
 * @(#)TaskHandlerService.java
 *
 * Copyright:       Copyright (c) 2017
 * Organisation:    opengrass.io aistac.io oatridge.io
 * Schema:          Adaptive, Intelligent, Single Task Application Concern (AI-STAC)
 */
package io.aistac.common.canonical.handler;

import io.aistac.common.canonical.log.LoggerQueueService;
import io.aistac.common.canonical.valueholder.ValueHolder;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The {@code TaskHandlerService} Class extends the methods of the parent class.
 *
 * @author Darryl Oatridge
 * @version 1.00 17-Apr-2016
 */
public class TaskHandlerService extends ValueHolder {

    private final static LoggerQueueService LOGGER = LoggerQueueService.getInstance();
    private final static String TASK = "CANONICAL.TASK";

    // Singleton Instance
    private volatile static TaskHandlerService INSTANCE;
    // monitor thread executor
    private final ExecutorService executor;
    private final ConcurrentSkipListMap<Integer, TaskHandlerInterface> taskHandlerMap;
    private final ConcurrentSkipListMap<Integer, Future<?>> futureMap;

    //<editor-fold defaultstate="collapsed" desc="Singleton Methods">
    // private Method to avoid instantiation externally
    private TaskHandlerService() {
        taskHandlerMap = new ConcurrentSkipListMap<>();
        futureMap = new ConcurrentSkipListMap<>();
        executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
    }

    /**
     * Singleton pattern to get the instance of the {@code TaskHandlerService} class
     *
     * @return instance of the {@code TaskHandlerService}
     */
    @SuppressWarnings("DoubleCheckedLocking")
    public static TaskHandlerService getInstance() {
        if(INSTANCE == null) {
            synchronized(TaskHandlerService.class) {
                // Check again just incase before we synchronised an instance was created
                if(INSTANCE == null) {
                    INSTANCE = new TaskHandlerService();
                }
            }
        }
        return INSTANCE;
    }
    //</editor-fold>

    /**
     * checks to see if the taskId is a running task
     *
     * @param taskId the task id
     * @return true if the task exists
     */
    public boolean isTask(int taskId) {
        return taskHandlerMap.containsKey(taskId);
    }

    /**
     * watches a task and returns when the task completes its computation
     *
     * @param taskId the id of the
     * @return true if ends, false if an Interrupt or Execution exception is thrown
     */
    public boolean watchTask(int taskId) {
        if(!isTask(taskId)) {
            // return true as assume it has been removed;
            return true;
        }
        try {
            futureMap.get(taskId).get();
            return true;
        } catch(InterruptedException | ExecutionException ex) {
            return false;
        }
    }

    /**
     * watches a task and returns when the task completes its computation or the timeout is reached.
     * On timeout a Timeout exception is thrown.
     *
     * @param taskId the id of the
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return true if ends, false if an Interrupt or Execution exception is thrown
     * @throws TimeoutException if the maximum time is reached
     */
    public boolean watchTask(int taskId, long timeout, TimeUnit unit) throws TimeoutException {
        if(!isTask(taskId)) {
            // return true as assume it has been removed;
            return true;
        }
        try {
            futureMap.get(taskId).get(timeout, unit);
            return true;
        } catch(InterruptedException | ExecutionException ex) {
            return false;
        }
    }

    /**
     * tests if a task is running
     *
     * @param taskId the task id
     * @return the isRunning boolean, true is running, false if not or doesn't exist
     */
    public boolean isTaskRunning(int taskId) {
        if(!isTask(taskId)) {
            return false;
        }
        return taskHandlerMap.get(taskId).isRunning();
    }

    /**
     * Register a task to be handled. The task must implement the {@code TaskHandlerInterface}. All tasks must be registered in order to run
     * within the Single Task Application Model
     *
     * @param task the task instance
     * @return the task reference id
     */
    public synchronized int setTaskHandler(TaskHandlerInterface task) {
        int rtnId = -1;
        if(task != null) {
            LOGGER.debug(TASK, "Register task handler " + task.getName());
            rtnId = uniqueFillId(taskHandlerMap.keySet(), 1, TimeUnit.SECONDS);
            if(!taskHandlerMap.containsKey(rtnId) || !taskHandlerMap.get(rtnId).isRunning()) {
                // lets make sure itr is all tidy
                taskHandlerMap.put(rtnId, task);
                futureMap.put(rtnId, executor.submit(taskHandlerMap.get(rtnId)));
                try {
                    if(!taskHandlerMap.get(rtnId).isRunning()) {
                        TimeUnit.MILLISECONDS.timedWait(this, 200);
                    }
                } catch(InterruptedException ex) {
                    // carry on
                }

            }
        }
        return rtnId;
    }

    /**
     * Stops a task with the given identity. This identity was retrieved when the task was registered by calling {@code getIdentity} as part
     * of the {@code TaskHandlerInterface}
     *
     * @param taskId the identifier of the task
     */
    public synchronized void stopTaskHandler(int taskId) {
        if(!isTask(taskId)) {
            throw new IllegalArgumentException("The task with Id [" + taskId + "] does not exist");
        }
        LOGGER.debug(TASK, "Stop task handler " + taskHandlerMap.get(taskId).getName());
        // shut down the monitor
        taskHandlerMap.get(taskId).stop();
        // make sure the thread is properly closed
        if(futureMap.containsKey(taskId)) {
            try {
                if(!futureMap.get(taskId).isDone()) {
                    // wait for it to end
                    futureMap.get(taskId).get(1, TimeUnit.SECONDS);
                }
            } catch(TimeoutException ex) {
                // it timed out just cancel it
                futureMap.get(taskId).cancel(true);
            } catch(InterruptedException | ExecutionException ex) {
                // ignore
            }
        }
        // remove the references
        taskHandlerMap.remove(taskId);
        futureMap.remove(taskId);
    }

    /**
     * shuts down all the the running tasks.
     */
    public void stopAllTasks() {
        taskHandlerMap.keySet().stream().forEach((id) -> {
            stopTaskHandler(id);
        });
    }

    /**
     * static to register a task handler.
     *
     * @param task the task to register
     * @return the integer identifier for the task.
     */
    public static int registerTaskHandler(TaskHandlerInterface task) {
        return TaskHandlerService.getInstance().setTaskHandler(task);
    }

}
