
/*
 * @(#)MessageQueue.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.data.ObjectBean;
import static io.aistac.common.canonical.data.ObjectBean.XmlFormat.PRINTED;
import static io.aistac.common.canonical.data.ObjectBean.XmlFormat.TRIMMED;
import io.aistac.common.canonical.exceptions.ObjectBeanException;
import io.aistac.common.canonical.log.LoggerQueueService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The {@code MessageQueue} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 22-Mar-2016
 * @param <T> the ObjectBean type to queue
 */
public class ObjectBeanQueue<T extends ObjectBean> extends LinkedBlockingQueue<T> {

    private static final long serialVersionUID = -335585515331551984L;

    private final static LoggerQueueService LOGGER = LoggerQueueService.getInstance();
    private final static String QUEUE = "CANONICAL.QUEUE";

    /**
     * Creates a {@code MessageQueue} with a capacity of {@link Integer#MAX_VALUE}.
     */
    public ObjectBeanQueue() {
        super();
    }

    /**
     * Creates a {@code LinkedBlockingQueue} with the given (fixed) capacity.
     * If the capacity is less than 1 then capacity is set to {@link Integer#MAX_VALUE}
     *
     * @param capacity the capacity of this queue
     */
    public ObjectBeanQueue(int capacity) {
        super(capacity > 0 ? capacity : Integer.MAX_VALUE);
    }

    /**
     * Accepts an XML representation of an {@code ObjectBean} and converts before adding it
     * into this queue if it is possible to do so immediately without violating capacity restrictions, returning
     * <tt>true</tt> upon success and throwing an <tt>IllegalStateException</tt>
     * if no space is currently available.
     *
     * <p>This implementation returns <tt>true</tt> if <tt>offer</tt> succeeds,
     * else throws an <tt>ObjectBeanException</tt>.
     *
     * @param xmlString the xml to convert and add
     * @return <tt>true</tt> upon success
     * @throws ObjectBeanException if the ObjectBean cannot be added at this time due to capacity restrictions
     * @throws NullPointerException if the specified element is null and this queue does not permit null elements
     */
    public boolean add(String xmlString) throws ObjectBeanException {
        return this.add(T.buildObjectBean(xmlString));
    }

    /**
     * Accepts an XML representation of an {@code ObjectBean} and converts before adding it at the tail of this queue,
     * waiting if necessary for space to become available.
     *
     * @param xmlString the xml to convert and put
     * @throws ObjectBeanException if unable to convert the xml to a {@code ObjectBean}
     * @throws NullPointerException if the {@code ObjectBean}
     * @throws InterruptedException the request is interrupted
     */
    public void put(String xmlString) throws ObjectBeanException, InterruptedException {
        this.put(T.buildObjectBean(xmlString));
    }

    /**
     * Accepts an XML representation of an {@code ObjectBean} and converts before
     * Inserting the specified {@code ObjectBean} at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning {@code true} upon success and {@code false} if this queue
     * is full.
     * When using a capacity-restricted queue, this method is generally
     * preferable to method add, which can fail to
     * insert an element only by throwing an exception.
     *
     * @param xmlString the XML to offer
     * @return true is offer accepted
     * @throws ObjectBeanException if the XML conversion fails
     * @throws NullPointerException if the {@code ObjectBean}
     */
    public boolean offer(String xmlString) throws ObjectBeanException {
        return this.offer(T.buildObjectBean(xmlString));
    }

    /**
     * Inserts the specified element at the tail of this queue, waiting if
     * necessary up to the specified wait time for space to become available.
     *
     * @param xmlString the XML to offer
     * @param timeout the timeout value
     * @param unit the unit of the timeout value
     * @return {@code true} if successful, or {@code false} if the specified waiting time elapses before space is available
     * @throws NullPointerException if the {@code ObjectBean}
     * @throws ObjectBeanException if the XML conversion fails
     * @throws InterruptedException the request is interrupted
     */
    public boolean offer(String xmlString, long timeout, TimeUnit unit) throws InterruptedException, ObjectBeanException {
        return this.offer(T.buildObjectBean(xmlString), timeout, unit);
    }

    /**
     * Retrieves and removes the head of this queue as an XML string, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    public String takeXML() throws InterruptedException {
       return this.take().toXML();
    }

    /**
     * Retrieves and removes the head of this queue as an XML string, waiting up to the
     * specified wait time if necessary for an element to become available.
     *
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return the head of this queue as an XML string, or {@code null} if the
     *         specified waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    public String pollXml(long timeout, TimeUnit unit) throws InterruptedException  {
        return this.poll(timeout, unit).toXML();
    }

    /**
     * checks to see if there are still elements in the queue
     *
     * @return true if there are still elements
     */
    public boolean hasRemaining() {
        return !this.isEmpty();
    }

    @Override
    public T poll() {
        LOGGER.debug(QUEUE, "Poll from Queue");
        T ob = super.poll();
        LOGGER.debug(QUEUE, "Recived from Queue");
        LOGGER.trace(QUEUE, "XML: " + ob.toXML(PRINTED, TRIMMED));
        return ob;
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        LOGGER.debug(QUEUE, "Poll from Queue with timeout");
        T ob = super.poll(timeout, unit);
        LOGGER.debug(QUEUE, "Recived from Queue");
        LOGGER.trace(QUEUE, "XML: " + ob.toXML(PRINTED, TRIMMED));
        return ob;
    }

    @Override
    public T take() throws InterruptedException {
        LOGGER.debug(QUEUE, "Take from Queue");
        T ob = super.take();
        LOGGER.debug(QUEUE, "Recived from Queue");
        LOGGER.trace(QUEUE, "XML: " + ob.toXML(PRINTED, TRIMMED));
        return ob;
    }

    @Override
    public void put(T e) throws InterruptedException {
        LOGGER.debug(QUEUE, "Put to Queue");
        LOGGER.trace(QUEUE, "XML: " + e.toXML(PRINTED, TRIMMED));
        super.put(e);
    }

    @Override
    public boolean offer(T e) {
        LOGGER.debug(QUEUE, "Offer to Queue");
        LOGGER.trace(QUEUE, "XML: " + e.toXML(PRINTED, TRIMMED));
       return super.offer(e);
    }

    @Override
    public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
        LOGGER.debug(QUEUE, "Offer to Queue ith timeout");
        LOGGER.trace(QUEUE, "XML: " + e.toXML(PRINTED, TRIMMED));
        return super.offer(e, timeout, unit);
    }
}
