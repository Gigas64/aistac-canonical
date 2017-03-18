/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.aistac.common.canonical.handler;

import io.aistac.common.canonical.handler.TaskHandlerService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class TaskHandlerServiceTest {

    public TaskHandlerServiceTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testSingleTask() throws Exception {
        ExampleTaskHandler task = new ExampleTaskHandler();
        int taskId = TaskHandlerService.registerTaskHandler(task);
        TimeUnit.SECONDS.sleep(1);
        assertThat(task.isRunning(), is(true));
        assertThat(TaskHandlerService.getInstance().isTask(taskId), is(true));
        TaskHandlerService.getInstance().stopTaskHandler(taskId);
        assertThat(task.isRunning(), is(false));
        assertThat(TaskHandlerService.getInstance().isTask(taskId), is(false));
    }

    @Test
    public void testMultipleTask() throws Exception {
        ExampleTaskHandler task1 = new ExampleTaskHandler();
        ExampleTaskHandler task2 = new ExampleTaskHandler();
        ExampleTaskHandler task3 = new ExampleTaskHandler();

        int taskId1 = TaskHandlerService.registerTaskHandler(task1);
        int taskId2 = TaskHandlerService.registerTaskHandler(task2);
        int taskId3 = TaskHandlerService.registerTaskHandler(task3);

        TimeUnit.SECONDS.sleep(1);

        assertThat(task1.isRunning(), is(true));
        assertThat(TaskHandlerService.getInstance().isTask(taskId1), is(true));
        assertThat(task2.isRunning(), is(true));
        assertThat(TaskHandlerService.getInstance().isTask(taskId2), is(true));
        assertThat(task3.isRunning(), is(true));
        assertThat(TaskHandlerService.getInstance().isTask(taskId3), is(true));

        TaskHandlerService.getInstance().stopAllTasks();

        assertThat(task1.isRunning(), is(false));
        assertThat(TaskHandlerService.getInstance().isTask(taskId1), is(false));
        assertThat(task2.isRunning(), is(false));
        assertThat(TaskHandlerService.getInstance().isTask(taskId2), is(false));
        assertThat(task3.isRunning(), is(false));
        assertThat(TaskHandlerService.getInstance().isTask(taskId3), is(false));
    }

    @Test
    public void testNoTaskId() throws Exception {
        assertThat(TaskHandlerService.getInstance().isTaskRunning(-1), is(false));
        assertThat(TaskHandlerService.getInstance().isTask(-1), is(false));
        try {
            TaskHandlerService.getInstance().stopTaskHandler(-1);
            fail();
        } catch(IllegalArgumentException e) {
        }
        assertThat(TaskHandlerService.getInstance().watchTask(-1), is(true));
    }


}