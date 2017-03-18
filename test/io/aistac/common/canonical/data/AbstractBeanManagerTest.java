/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.aistac.common.canonical.data;

// common imports
import io.aistac.common.canonical.queue.ObjectBeanQueue;
import io.aistac.common.canonical.data.example.ExampleBean;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Darryl Oatridge
 */
public class AbstractBeanManagerTest {

    private ExampleManager manager;
    private final String owner = "owner";


    @Test
    public void testPersistence() throws Exception {
        manager = new ExampleManager();


    }


    private void setExampleData()  {
        manager.setObject(0, new ExampleBean(2, 11, "Z", owner));
        manager.setObject(1, new ExampleBean(3, 101, "AA", owner));
        manager.setObject(1, new ExampleBean(4, 102, "AB", owner));
        manager.setObject(1, new ExampleBean(5, 103, "AC", owner));
        manager.setObject(2, new ExampleBean(3, 102, "AC", owner));
        manager.setObject(2, new ExampleBean(5, 103, "AC", owner));
    }
}
