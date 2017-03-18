/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.aistac.common.canonical.queue;

import io.aistac.common.canonical.queue.ObjectBeanQueue;
import io.aistac.common.canonical.exceptions.ObjectBeanException;
import io.aistac.common.canonical.data.example.ExampleBean;
import io.aistac.common.canonical.data.example.BeanBuilder;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class ObjectBeanQueueTest {

    ObjectBeanQueue<ExampleBean> queue;

    public ObjectBeanQueueTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAdd() throws Exception {
        queue = new ObjectBeanQueue<>();
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean());

        assertThat(queue.add(bean.toXML()), is(true));
        assertThat(queue.take(),is(bean));
        try {
            queue.add((String)null);
            fail();
        } catch(ObjectBeanException e) {
            // success
        }
        try {
            queue.add("");
            fail();
        } catch(ObjectBeanException e) {
            // success
        }
    }

    @Test
    public void testPut() throws Exception {
        queue = new ObjectBeanQueue<>();
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean());

        queue.put(bean.toXML());
        assertThat(queue.take(),is(bean));
    }

    @Test
    public void testOffer_String() throws Exception {
        queue = new ObjectBeanQueue<>();
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean());

        assertThat(queue.offer(bean.toXML()), is(true));
        assertThat(queue.take(),is(bean));
    }

    @Test
    public void testOffer_3args() throws Exception {
        queue = new ObjectBeanQueue<>();
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean());

        assertThat(queue.offer(bean.toXML(),1, TimeUnit.SECONDS), is(true));
        assertThat(queue.take(),is(bean));
    }

    @Test
    public void testTakeXML() throws Exception {
        queue = new ObjectBeanQueue<>();
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean());
        String xmlBean = bean.toXML();

        queue.add(bean);
        assertThat(queue.takeXML(),is(equalTo(xmlBean)));
    }

    @Test
    public void testPollXml() throws Exception {
        queue = new ObjectBeanQueue<>();
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean());
        String xmlBean = bean.toXML();

        queue.add(bean);
        assertThat(queue.pollXml(1, TimeUnit.SECONDS),is(equalTo(xmlBean)));
    }

}