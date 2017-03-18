/*
 * @(#)ObjectEnum.java
 *
 * Copyright:	Copyright (c) 2010
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.data;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code ObjectEnum} Enumeration used for general consumption with regard to the utility store classes
 *
 * @author Darryl Oatridge
 * @version 1.00 14-Jul-2010
 */
public enum ObjectEnum {

    DEFAULT_ID(1),
    DEFAULT_KEY(1),
    DEFAULT_VALUE(0),
    INITIALISATION(-1),
    SINGLE_KEY(-2),
    SINGLE_YWD(-3),
    MIN_RESERVED(-9),
    MAX_RESERVED(1);

    private final int value;

    ObjectEnum(int value) {
        this.value = value;
    }

    /**
     * The value of this enumeration
     *
     * @return the value associated with an enumeration
     */
    public int value() {
        return (this.value);
    }

    /**
     * A check to see if a value is a reserved value
     *
     * @param i value to check
     * @return true if is reserved else false
     */
    public static boolean isReserved(int i) {
        if(i < MIN_RESERVED.value() || i > MAX_RESERVED.value()) {
            return (false);
        }
        return (true);
    }

    private static AtomicInteger counter = new AtomicInteger(1000);

    /**
     * Used to generate a unique instance id. these numbers will be reset
     * at the start of each instance run
     *
     * @return a unique instance id.
     */
    public static int instanceId() {
        return counter.incrementAndGet();
    }
}
