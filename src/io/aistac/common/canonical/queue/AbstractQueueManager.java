 /*
 * @(#)ObjectBeanQueueMap.java
 *
 * Copyright:       Copyright (c) 2017
 * Organisation:    opengrass.io aistac.io oatridge.io
 * Schema:          Adaptive, Intelligent, Single Task Application Concern (AI-STAC)
 */
package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.data.ObjectBean;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The {@code ObjectBeanQueueMap} is an abstract class ready for a singleton of a specific queue .
 *
 * @author Darryl Oatridge
 * @version 1.00 25-Mar-2016
 * @param <T> an extended ObjectBean
 */
public abstract class AbstractQueueManager<T extends ObjectBean> {

    // the Queue map
    private final ConcurrentSkipListMap<String, ObjectBeanQueue<T>> queueMap = new ConcurrentSkipListMap<>();
    // the maximum queue capacity
    protected static final int MAX_CAPACITY = Integer.MAX_VALUE;

    /*
     * protected method to allow getQueue() to be individually unsupported if need be
     */
    protected ObjectBeanQueue<T> createQueue(String queueName, int capacity) {
        ObjectBeanQueue<T> rtnQueue = null;
        if(queueName != null) {
            if(!queueMap.containsKey(queueName)) {
                queueMap.put(queueName, new ObjectBeanQueue<>(capacity));
            }
            rtnQueue = queueMap.get(queueName);
        }
        return rtnQueue;
    }

    /**
     * returns a list of all the names of {@code ObjectBeanQueue<T>} queues being held
     *
     * @return a set of names
     */
    public Set<String> getNameSet() {
        return queueMap.keySet();
    }

    /**
     * removes a identity {@code ObjectBeanQueue<T>} queue for the specific queueName
     *
     * @param queueName the name to be removed
     * @return the previous value associated with the specified key, or null if there was no mapping for the key
     */
    public ObjectBeanQueue<T> removeQueue(String queueName) {
        return queueName != null ? queueMap.remove(queueName) : null;
    }

}
