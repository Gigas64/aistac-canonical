/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * @(#)ExampleQueueManagerImp.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.data.example.ExampleBean;
import io.aistac.common.canonical.properties.TaskPropertiesService;
import io.aistac.common.canonical.queue.AbstractQueueManager;
import io.aistac.common.canonical.queue.ObjectBeanQueue;

/**
 * The {@code ExampleQueueManagerImp} Class extends the methods of the parent class.
 *
 * @author Darryl Oatridge
 * @version 1.00 06-Apr-2016
 */
public class ExampleQueueManagerImp extends AbstractQueueManager<ExampleBean> {
    // Singleton Instance
    private volatile static ExampleQueueManagerImp INSTANCE;

    //<editor-fold defaultstate="expanded" desc="Singletone Methods">
    // private Method to avoid instantiation externally
    private ExampleQueueManagerImp() {
        // this should be empty
    }

    /**
     * Singleton pattern to get the instance of the {@code ExampleQueueManagerImp} class
     * @return instance of the {@code ExampleQueueManagerImp}
     */
    @SuppressWarnings("DoubleCheckedLocking")
    public static ExampleQueueManagerImp getInstance() {
        if(INSTANCE == null) {
            synchronized (ExampleQueueManagerImp.class) {
                // Check again just incase before we synchronised an instance was created
                if(INSTANCE == null) {
                    INSTANCE = new ExampleQueueManagerImp();
                }
            }
        }
        return INSTANCE;
    }

    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Public Queue Methods">
    /* ***************************************************
     * P U B L I C   Q U E U E   M E T H O D S
     * ***************************************************/

    /**
     * returns an {@code ObjectBeanQueue<ExampleBean>} queue. If the identity does not exist a queue is
     * created therfore never returns null and not requiring a set up before using
     *
     * @param queueName the name of the queue
     * @return the requested {@code ObjectBeanQueue<ExampleBean>} queue
     */
    public ObjectBeanQueue<ExampleBean> getQueue(String queueName) {
        return this.createQueue(queueName, MAX_CAPACITY);
    }

    /**
     * returns an {@code ObjectBeanQueue<ExampleBean>} queue. If the queueName does not exist a queue is
     * created therfore never returns null and not requiring a set up before using.
     * The queue will be limited to capacity.
     *
     * @param queueName the name of the queue
     * @param capacity the capacity of the queue
     * @return the requested {@code ObjectBeanQueue<ExampleBean>} queue
     */
    public ObjectBeanQueue<ExampleBean> getQueue(String queueName, int capacity) {
        return this.createQueue(queueName, capacity);
    }


    /**
     * Abbreviated static version of {@code getQueue(queueName)}. Returns an {@code ObjectBeanQueue<ExampleBean>} queue.
     * If the name does not exist a queue is created therfore never returns null and not requiring a set up before using
     *
     * @param queueName the name of the queue
     * @return the requested {@code ObjectBeanQueue<ExampleBean>} queue
     */
    public static ObjectBeanQueue<ExampleBean> queue(String queueName) {
        return ExampleQueueManagerImp.getInstance().getQueue(queueName);
    }

    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Private Class Methods">
    /* ***************************************************
     * P R I V A T E   C L A S S   M E T H O D S
     * ***************************************************/


    //</editor-fold>


 }
