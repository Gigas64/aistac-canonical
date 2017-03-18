/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.queue.ObjectBeanQueue;
import io.aistac.common.canonical.data.example.ExampleBean;
import io.aistac.common.canonical.data.example.BeanBuilder;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class AbstractQueueMapTest {

    @Test
    public void testMethods() throws Exception {

        ObjectBeanQueue<ExampleBean> qOne = ExampleQueueManagerImp.queue("one");
        ObjectBeanQueue<ExampleBean> qTwo = ExampleQueueManagerImp.queue("two");
        ObjectBeanQueue<ExampleBean> qSix = ExampleQueueManagerImp.queue("six");
        assertThat(ExampleQueueManagerImp.getInstance().getNameSet().size(), is(3));

        // add a bean to test we get a queue
        ExampleBean testBean = new ExampleBean();
        qTwo.add(testBean);
        ExampleBean take1 = qTwo.take();
        assertThat(take1, is(equalTo(testBean)));

        // get the queue again and check it is the same
        qTwo.add(testBean);
        ObjectBeanQueue<ExampleBean> qTest = ExampleQueueManagerImp.queue("two");
        ExampleBean take2 = qTest.take();
        assertThat(take2, is(equalTo(testBean)));


        // remove the one in the middle
        ExampleQueueManagerImp.getInstance().removeQueue("two");
        assertThat(ExampleQueueManagerImp.getInstance().getNameSet().size(), is(2));
    }

    /*
     *
     */
    @Test
    public void testThreadQueue() throws Exception {
        int seed = 1;
        String nOne = "nOne";
        String tester = "tester";
        ObjectBeanQueue<ExampleBean> qOne = ExampleQueueManagerImp.queue(nOne);
        Runnable r = () -> {
            try {
                ObjectBeanQueue<ExampleBean> tOne = ExampleQueueManagerImp.queue(nOne);
                ExampleBean take = tOne.take();
                assertThat(take.getId(),is(5));
                take = tOne.take();
                assertThat(take.getId(),is(2));
                take = tOne.take();
                assertThat(take.getId(),is(12));
            } catch(Exception ex) {
                // just exit;
            }
        };
        Thread t = new Thread(r);
        t.start();

        qOne.add((ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 5));
        qOne.add((ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 2));
        qOne.add((ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 12));
        t.join();
    }


}
