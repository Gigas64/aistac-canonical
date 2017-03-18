/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * @(#)ExampleTaskHandler.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.handler;

import io.aistac.common.canonical.handler.TaskHandlerInterface;
import java.util.concurrent.TimeUnit;

/**
 * The {@code ExampleTaskHandler} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 19-Apr-2016
 */
public class ExampleTaskHandler implements TaskHandlerInterface {
    private boolean isRunning = false;

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;
        while(isRunning) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch(InterruptedException ex) {
                isRunning = false;
            }
        }
        isRunning = false;
    }

    @Override
    public String getName() {
        return "ExampleTaskHandler";
    }

}
