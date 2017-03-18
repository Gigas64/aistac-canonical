/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.aistac.common.canonical.log;

import io.aistac.common.canonical.log.LoggerBean;
import io.aistac.common.canonical.data.BeanTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class LoggerBeanTest {

    public LoggerBeanTest() {
    }
    /**
     * Tests the underlying bean.
     */
    @Test
    public void test_LoggerBean() throws Exception {
        boolean isPrintXML = false;
        boolean isGroupKey = false;
//            List<String> exemptMethods = Stream.of("getId", "getKey").collect(Collectors.toList());
        BeanTester.testObjectBean(LoggerBean.class.getName(), isPrintXML, isGroupKey);
    }
}
