/*
 * @(#)TaskHandlerInterface.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.handler;

/**
 * The {@code TaskHandlerInterface} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 01-Apr-2016
 */
public interface TaskHandlerInterface extends Runnable {

    /**
     * @return an identifying name to help track the task
     */
    public String getName();

    /**
     * @return true if the task is running
     */
    public boolean isRunning();

    /**
     * stops the task
     */
    public void stop();
}
