/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * @(#)QueueDeliveryMonitor.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.data.ObjectBean;
import io.aistac.common.canonical.handler.TaskHandlerInterface;
import io.aistac.common.canonical.log.LoggerQueueService;
import java.util.concurrent.TimeUnit;


/**
 * The {@code QueueDeliveryMonitor} Class is the ObjectStore Janitor. It watches queues and updates the data store when needed
 *
 * @author Darryl Oatridge
 * @version 1.00 24-Mar-2016
 * @param <T> the type of {@code ObjectBean}
 */
public class QueueDeliveryMonitor<T extends ObjectBean> implements Runnable, TaskHandlerInterface {

    private final static LoggerQueueService LOGGER = LoggerQueueService.getInstance();
    private final static String MONITOR = "CANONICAL.QUEUE.MONITOR";

    private volatile boolean isRunning;
    private final QueueDeliveryInterface<T> targer;
    private final ObjectBeanQueue<T> queue;
    private final String queueName;

    /**

* The {@code QueueDeliveryInterface} must be implemented to use the monitor. The monitor will call the
     * Functional Interface method when a queue item appears.
     *
     *
     * @param queueName the referencing name of the queue
     * @param queueIn the in queue of the class implementing QueueManagerInterface
     * @param target the target instance that has implemented the {@code QueueDeliveryInterface}
     */
    public QueueDeliveryMonitor(String queueName, ObjectBeanQueue<T> queueIn, QueueDeliveryInterface<T> target) {
        this.queue = queueIn;
        this.targer = target;
        this.queueName = queueName;
    }

    @Override
    public void run() {
        LOGGER.debug(MONITOR, "Starting Queue Monitor [" + queueName + "]...");
        isRunning = true;
        while(isRunning || !Thread.interrupted()) {
            T ob;
            try {
                // sit on the queue and wait for something to happen
                ob = queue.poll(60, TimeUnit.SECONDS);
                if(ob != null) {
                    LOGGER.trace(MONITOR, "delivery " + ob.getClass().getSimpleName() + " id:key [" + ob.getIdentifier() + ":" + ob.getGroupKey() + "] from queue [" + queueName + "]");
                    targer.deliver(ob);
                }
            } catch(InterruptedException | NullPointerException | AssertionError ex) {
                isRunning = false;
            }
        }
        // just to make sure
        isRunning = false;
        LOGGER.debug(MONITOR, "Exiting Queue Monitor [" + queueName + "]");
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * used to stop the monitor run thread
     */
    @Override
    public void stop() {
        LOGGER.debug(MONITOR, "Request to stop Queue Monitor [" + queueName + "]");
        isRunning = false;
    }

    @Override
    public String getName() {
        return "QueueDeliveryMonitor:[" + queueName + "]";
    }
}
