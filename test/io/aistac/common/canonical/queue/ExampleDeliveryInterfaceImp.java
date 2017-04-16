/*
 * @(#)ExampleDeliveryInterfaceImp.java
 *
 */
package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.queue.QueueDeliveryInterface;
import io.aistac.common.canonical.queue.ObjectBeanQueue;
import io.aistac.common.canonical.data.example.ExampleBean;

/**
 * The {@code ExampleDeliveryInterfaceImp} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 05-Apr-2016
 */
public class ExampleDeliveryInterfaceImp implements QueueDeliveryInterface<ExampleBean> {

    private final ObjectBeanQueue<ExampleBean> queueIn;
    private final ObjectBeanQueue<ExampleBean> queueOut;
    private ExampleBean delivery;

    public ExampleDeliveryInterfaceImp() {
        queueIn = ExampleQueueManagerImp.queue("ExampleDeliveryImp.queueIn");
        queueOut = ExampleQueueManagerImp.queue("ExampleDeliveryImp.queueOut");
    }

    public String getQueueIn() {
        return "ExampleDeliveryImp.queueIn";
    }

    public String getQueueOut() {
        return "ExampleDeliveryImp.queueOut";
    }

    public ExampleBean getDelivery() {
        return delivery;
    }

    @Override
    public void deliver(ExampleBean ob) {
        delivery = ob;
    }

    public void send(ExampleBean ob) {
        startQueueMonitor("queueIn", queueIn);
        queueOut.add(ob);
    }

}
