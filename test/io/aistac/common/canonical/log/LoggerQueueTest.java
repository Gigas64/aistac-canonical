/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.aistac.common.canonical.log;

import io.aistac.common.canonical.log.LoggerQueueService;
import static io.aistac.common.canonical.log.LoggerLevel.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Darryl Oatridge
 */
public class LoggerQueueTest {

    LoggerQueueService LOGGER;
    private String name = LoggerQueueTest.class.getName();

    /*
     *
     */
    @Test
    public void testLogging() throws Exception {
        LOGGER = LoggerQueueService.getInstance();
        // test the log level
        assertThat(LOGGER.getLogLevel(), is(ERROR));
        LOGGER.setLogLevel(INFO);
        assertThat(LOGGER.getLogLevel(), is(INFO));
        assertThat(LOGGER.getLogLevel(), is(not(DEBUG)));
        assertThat(LOGGER.getLogLevel(), is(not(WARN)));

        LOGGER.setLogLevel(ERROR);
        assertThat(LOGGER.getLogLevel(), is(not(INFO)));
        assertThat(LOGGER.getLogLevel(), is(ERROR));
        assertThat(LOGGER.getLogLevel(), is(ERROR));

        // Get the intial message
        LOGGER.setLogLevel(INFO);
        assertThat(LOGGER.getLogLevel(), is(INFO));
        LOGGER.info("INFO", "The test has started");
        assertThat(LOGGER.hasRemaining(), is(true));
        assertThat(LOGGER.pendingLogs(), is(1));
        LOGGER.clearLogs();
        assertThat(LOGGER.hasRemaining(), is(false));
        assertThat(LOGGER.pendingLogs(), is(0));
        // add one above the logging level
        LOGGER.debug("TEST", "This is a DEBUG message");
        assertThat(LOGGER.hasRemaining(), is(false));
        assertThat(LOGGER.pendingLogs(), is(0));
        // add one on
        LOGGER.error("TEST", "This is an INFO message");
        assertThat(LOGGER.hasRemaining(), is(true));
        assertThat(LOGGER.pendingLogs(), is(1));
    }

}
