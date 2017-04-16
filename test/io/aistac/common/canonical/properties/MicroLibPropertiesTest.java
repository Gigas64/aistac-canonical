/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.aistac.common.canonical.properties;

import io.aistac.common.canonical.properties.TaskPropertiesService;
import io.aistac.common.canonical.data.BeanTester;
import io.aistac.common.canonical.data.example.ExampleBean;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Darryl Oatridge
 */
public class MicroLibPropertiesTest {

    private TaskPropertiesService props;
    private String hello = "Hello World";
    private long time = System.currentTimeMillis();
    private int max = Integer.MAX_VALUE;

    public MicroLibPropertiesTest() {
    }

    @Before
    public void setUp() {

        props = TaskPropertiesService.getInstance();
        props.add("String", hello);
        props.add("Long", time);
        props.add("Int", max);

    }

    @After
    public void tearDown() {
        TaskPropertiesService.removeInstance();
    }

    @Test
    public void testProperties() {
        assertThat(props.get("Bad Name"), is(nullValue()));
        assertThat(TaskPropertiesService.getInstance().get("Bad Name"), is(nullValue()));
        assertThat(props.get("Bad Name", "Good Name"), is(equalTo("Good Name")));
        assertThat(TaskPropertiesService.getInstance().get("Bad Name", "Good Name"), is(equalTo("Good Name")));
        assertThat(TaskPropertiesService.getProp("Bad Name", "Good Name"), is(equalTo("Good Name")));
        assertThat(props.getInt("String", -1), is(equalTo(-1)));
        assertThat(props.getLong("Bad Name", -1), is(equalTo(-1L)));
        assertThat(props.getInt("Bad Name", -1), is(equalTo(-1)));

        assertThat(props.get("String"), is(equalTo(hello)));
        assertThat(props.get("Long"), is(equalTo(Long.toString(time))));
        assertThat(props.get("Int"), is(equalTo(Integer.toString(max))));

        assertThat(props.getLong("Long", -1), is(equalTo(time)));
        assertThat(props.getInt("Int", -1), is(equalTo(max)));

        assertThat(TaskPropertiesService.getProp("String",""), is(equalTo(hello)));

    }

    /*
     *
     */
    @Test
    public void testStringBadParams() throws Exception {
        try {
            props.add(null, hello);
            fail();
        } catch(NullPointerException e) {
        }
        try {
            props.add("", hello);
            fail();
        } catch(NullPointerException e) {
        }
        try {
            props.add(null, null);
            fail();
        } catch(NullPointerException e) {
        }
        try {
            props.add("String", null);
            fail();
        } catch(NullPointerException e) {
        }

    }

    /*
     *
     */
    @Test
    public void testIntBadParams() throws Exception {
        try {
            props.add(null, max);
            fail();
        } catch(NullPointerException e) {
        }
        try {
            props.add("", max);
            fail();
        } catch(NullPointerException e) {
        }
        try {
            props.add(null, null);
            fail();
        } catch(NullPointerException e) {
        }
        try {
            props.add("Int", null);
            fail();
        } catch(NullPointerException e) {
        }
        try {
            props.add("Int", new ExampleBean());
            fail();
        } catch(ClassCastException e) {
        }

    }

    @Test
    public void testContains() {
        assertThat(props.contains("aistac.canonical.objectbean.xml.root"), is(true));
        assertThat(props.contains("NoProp"), is(false));
    }

}
