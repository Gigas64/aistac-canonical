/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package io.aistac.common.canonical.data.example;

import io.aistac.common.canonical.data.BeanTester;
import io.aistac.common.canonical.data.example.ExampleBean;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Darryl
 */
public class ExampleBeanTest {

    @Test
    public void testXMLDOM() {
        ExampleBean bean = new ExampleBean(1, 101, "TestBean", "tester");
        String xml = bean.toXML();
        ExampleBean other = new ExampleBean();
        other.setXMLDOM(bean.getXMLDOM().detachRootElement());
        assertEquals(bean, other);
        assertEquals(xml, other.toXML());
    }

    @Test
    public void test() throws Exception {
        String className = ExampleBean.class.getName();
        BeanTester.testObjectBean(className, false, false);
    }
}