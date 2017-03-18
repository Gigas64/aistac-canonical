 /*
 * @(#)DataQueueInterface.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.data.ObjectBean;
import io.aistac.common.canonical.handler.TaskHandlerService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The {@code DataQueueInterface} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 25-Mar-2016
 * @param <T> the type of {@code ObjectBean}
 */
public interface QueueDeliveryInterface<T extends ObjectBean> {

    /**
     * when an object implements the {@code QueueDeliveryInterface} incoming {@code T ObjectBean} objects will be delivered to this method.
     * The delivery will come from the queue defined by calling {@code startQueueMonitor}
     *
     * @param ob the delivered T bean
     */
    public abstract void deliver(T ob);

    /**
     * Start the queue monitor using the {@code ObjectBeanQueue<T>}. This should be the queueIn as the target
     * of the delivery, by convention.
     *
     * @param name the name of the queue
     * @param queue the {@code ObjectBeanQueue<T>} to
     * @return the taskId
     */
    default int startQueueMonitor(String name, ObjectBeanQueue<T> queue) {
        Extensions.taskId = TaskHandlerService.registerTaskHandler(new QueueDeliveryMonitor<>(name, queue, this));
        return Extensions.taskId;
    }

    /**
     * @return true if the monitor thread is running, else false
     */
    default boolean isMonitorRunning() {
        return TaskHandlerService.getInstance().isTaskRunning(Extensions.taskId);
    }

    /**
     * watches a monitor and returns when the monitor completes its computation
     *
     * @return true if ends, false if an Interrupt or Execution exception is thrown
     */
    default boolean waitToStop() {
        return TaskHandlerService.getInstance().watchTask(Extensions.taskId);
    }

    /**
     * watches a monitor and returns when the monitor completes its computation or the timeout is reached.
     * On timeout a Timeout exception is thrown.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return true if ends, false if an Interrupt or Execution exception is thrown
     * @throws TimeoutException if the maximum time is reached
     */
    default boolean waitToStop(long timeout, TimeUnit unit) throws TimeoutException {
        return TaskHandlerService.getInstance().watchTask(Extensions.taskId, timeout, unit);
    }

    /**
     * stops the Queue Monitor
     */
    default void stopQueueMonitor() {
        TaskHandlerService.getInstance().stopTaskHandler(Extensions.taskId);
    }

    // the transport queue monitor and thread
    class Extensions {
        private static volatile int taskId = 0;

        private Extensions() {
        }
    }

}
