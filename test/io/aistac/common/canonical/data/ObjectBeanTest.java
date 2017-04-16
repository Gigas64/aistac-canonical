/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.aistac.common.canonical.data;

// common imports
import io.aistac.common.canonical.data.BeanTester;
import io.aistac.common.canonical.data.ObjectBean;
import io.aistac.common.canonical.exceptions.AiStacSchemaException;
import static io.aistac.common.canonical.data.ObjectBean.XmlFormat.*;
import io.aistac.common.canonical.data.example.BeanBuilder;
import io.aistac.common.canonical.data.example.ExampleBean;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import static java.util.Arrays.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
// Test Imports
import mockit.*;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl
 */
public class ObjectBeanTest {

    @Test
    public void testParameters() {
        long time = System.currentTimeMillis();
        // null owner should trhow an exception
        try {
            ExampleBean bean = new ExampleBean(97, 101, "TestBean", null);
            fail("should trhow an exceptiuon for null owner");
        } catch(IllegalArgumentException e) {
            // SUCCESS
        }

        // empty owner should trhow an exception
        try {
            ExampleBean bean = new ExampleBean(97, 101, "TestBean", "");
            fail("should trhow an exceptiuon for empty owner");
        } catch(IllegalArgumentException e) {
            // SUCCESS
        }

        ExampleBean bean = new ExampleBean(97, 101, "TestBean", "NO_VALUE");
        assertEquals(97, bean.getIdentifier());
        assertEquals(time, bean.getCreated(), 50);
        assertEquals(time, bean.getModified(), 50);
        assertEquals("NO_VALUE", bean.getOwner());

        long modified = bean.getModified();
        long created = bean.getCreated();
        assertEquals(created, bean.getCreated(), 0);
        assertEquals(modified, bean.getModified(), 0);
        bean.setOwner("tester");
        assertEquals(97, bean.getIdentifier());
        assertEquals(created, bean.getCreated(), 0);
        assertNotSame(modified, bean.getModified());
        assertEquals("tester", bean.getOwner());
    }

    @Test
    public void testBean() throws AiStacSchemaException {
        for(ObjectBeanTestList obe : ObjectBeanTestList.values()) {
            BeanTester.testObjectBean(obe.getCls(), obe.isPrintXml());
        }
    }
    @Test
    public void testClone() throws Exception {
        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean());
        // long clone
        ExampleBean longClone = bean.clone(11, 13, "New Owner");
        assertThat(bean.compareTo(bean), is(0));
        assertThat(bean.compareTo(longClone), is(not(0)));
        assertThat((longClone instanceof ExampleBean), is(true));
        assertThat(longClone.getId(), is(not(equalTo(bean.getId()))));
        assertThat(longClone.getKey(), is(not(equalTo(bean.getKey()))));
        assertThat(longClone.getOwner(), is(not(equalTo(bean.getOwner()))));
        assertThat(longClone.getValue(), is(equalTo(bean.getValue())));
        assertThat(longClone.getName(), is(equalTo(bean.getName())));
        // short clone
        ExampleBean shortClone = bean.clone(11);
        assertThat(bean.compareTo(bean), is(0));
        assertThat(bean.compareTo(shortClone), is(not(0)));
        assertThat((shortClone instanceof ExampleBean), is(true));
        assertThat(shortClone.getId(), is(not(equalTo(bean.getId()))));
        assertThat(shortClone.getKey(), is(equalTo(bean.getKey())));
        assertThat(shortClone.getOwner(), is(equalTo(bean.getOwner())));
        assertThat(shortClone.getValue(), is(equalTo(bean.getValue())));
        assertThat(shortClone.getName(), is(equalTo(bean.getName())));


    }

    /**
     * Unit test:
     */
    @Test
    public void testCompare() throws Exception {
        // create bean
        int seed = 0;
        ExampleBean bean04 = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 4);
        Thread.sleep(2);
        ExampleBean bean01 = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 1);
        Thread.sleep(2);
        ExampleBean bean03 = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 3);
        Thread.sleep(2);
        ExampleBean bean02 = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 2);
        LinkedList<ExampleBean> list = new LinkedList<ExampleBean>();
        list.add(bean01);
        list.add(bean02);
        list.add(bean03);
        list.add(bean04);
        testOrder(new int[]{1, 2, 3, 4}, list);
        Collections.sort(list, ObjectBean.MODIFIED_ORDER);
        testOrder(new int[]{4, 1, 3, 2}, list);
    }

//    /*
//     *
//     */
//    @Test
//    public void testToXml() throws Exception {
//        ExampleBean bean = (ExampleBean) BeanBuilder.addBeanValues(new ExampleBean(), 1);
//        System.out.println("pretty");
//        System.out.println(bean.toXML(PRETTY));
//        System.out.println("compacted");
//        System.out.println(bean.toXML());
//        System.out.println("trimmed");
//        System.out.println(bean.toXML(TRIMMED));
//        System.out.println("pretty trimmed");
//        System.out.println(bean.toXML(TRIMMED, PRETTY));
//        System.out.println("printed");
//        System.out.println(bean.toXML(PRINTED));
//        System.out.println("pretty printed");
//        System.out.println(bean.toXML(PRETTY, PRINTED));
//
//    }
    private void testOrder(int[] order, LinkedList<ExampleBean> check) {
        for(int i = 0; i < order.length; i++) {
            assertEquals("Testing order, layer [" + i + "]", order[i], check.get(i).getId());
        }
    }

}
