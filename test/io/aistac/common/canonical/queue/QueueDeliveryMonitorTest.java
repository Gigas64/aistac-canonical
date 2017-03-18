/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.queue.ObjectBeanQueue;
import io.aistac.common.canonical.data.example.BeanBuilder;
import io.aistac.common.canonical.data.example.ExampleBean;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class QueueDeliveryMonitorTest {


    @Test(timeout = 5000)
    public void testDelivery() throws Exception {
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(),1);
        ExampleBean test = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(),2);
        ExampleDeliveryInterfaceImp myDelivery = new ExampleDeliveryInterfaceImp();
        ObjectBeanQueue<ExampleBean> queueOut = ExampleQueueManagerImp.queue(myDelivery.getQueueOut());
        ObjectBeanQueue<ExampleBean> queueIn = ExampleQueueManagerImp.queue(myDelivery.getQueueIn());
        // get myDelivery to send
        myDelivery.send(bean);
        ExampleBean take = queueOut.take();
        assertThat(bean,is(take));

        queueIn.add(test);
        while(myDelivery.getDelivery() == null) {}
        assertThat(myDelivery.getDelivery(), is(test));

        myDelivery.stopQueueMonitor();
        // wait and see that it stops
        myDelivery.waitToStop();

        // now start it up again just to check
        // get myDelivery to send
        myDelivery.send(bean);
        take = queueOut.take();
        assertThat(bean,is(take));

        queueIn.add(test);
        while(myDelivery.getDelivery() == null) {}
        assertThat(myDelivery.getDelivery(), is(test));

        myDelivery.stopQueueMonitor();
        // wait and see that it stops
        myDelivery.waitToStop();

    }

}