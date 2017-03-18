/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * @(#)LoggerLevel.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.log;

/**
 * The {@code LoggerLevel} Enumeration
 *
 * @author Darryl Oatridge
 * @version 1.00 22-Mar-2016
 */
public enum LoggerLevel {
    /**
     * No events will be logged.
     */
    OFF(0),
    /**
     * A severe error that will prevent the application from continuing.
     */
    FATAL(1),

    /**
     * An error in the application, possibly recoverable.
     */
    ERROR(2),

    /**
     * An event that might possible lead to an error.
     */
    WARN(3),

    /**
     * An event for informational purposes.
     */
    INFO(4),

    /**
     * A general debugging event.
     */
    DEBUG(5),

    /**
     * A fine-grained debug message, typically capturing the flow through the application.
     */
    TRACE(6),
    /**
     * All events should be logged.
     */
    ALL(7);

    private final int value;

    LoggerLevel(int value) {
        this.value = value;
    }

    /**
     * The value of this enumeration
     * @return the value associated with an enumeration
     */
    public int value() {
        return (this.value);
    }

    /**
     * A check to see if the log level is less than or equal to the set level
     *
     * @param logLevel the level of the logging
     * @param setLevel the level set for logging
     * @return false if the log level is greater than the set level
     */
    public static boolean isLogged(LoggerLevel logLevel, LoggerLevel setLevel) {
        return logLevel.value() <= setLevel.value();
    }

    /**
     * Returns the name String of the passed logging level value.
     * @param value the integer value
     * @return a string name or UNDEFINED if not found
     */
    public static LoggerLevel level(int value) {
        for(LoggerLevel level : LoggerLevel.values()) {
            if(level.value() == value) {
                return level;
            }
        }
        return TRACE;
    }
}
