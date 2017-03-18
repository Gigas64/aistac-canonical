/*
 * @(#)AbstractBits.java
 *
 * Copyright:	Copyright (c) 2010
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.valueholder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code AbstractBits} Class match an abstract class with useful methods for the AbstractBits class
 *
 * @author Darryl Oatridge
 * @version 1.00 19-Nov-2011
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public abstract class AbstractBits {

    public static final int ALL_OFF = 0;
    public static final int NO_VALUE = 0;
    public static final int ALL_ON = (int) Math.pow(0x2, 0x1f);
    public static final int UNDEFINED = (int) -Math.pow(0x2, 0x1f);
    // duplicate names

    /**
     * Static method to test if every value bit exactly equals the equivalent mask bits Both values must be positive integers.
     *
     * @param bitValue the bit value to test
     * @param mask the mask to put over the bit value
     * @return true if bitValue equals mask
     */
    public static boolean equals(int bitValue, int mask) {
        if(bitValue < 0 || mask < 0) {
            return (false);
        }
        return bitValue == mask;
    }

    /**
     * Static method to test if the value bits all match the equivalent mask bits Both values must be positive integers.
     *
     * @param bitValue the bit value to test
     * @param mask the mask to put over the bit value
     * @return true if logical AND returns the same value as the mask
     */
    public static boolean match(int bitValue, int mask) {
        if(bitValue < 0 || mask < 0) {
            return (false);
        }
        return (bitValue & mask) == mask;
    }

    /**
     * Static method to test if the value bits part matches (at least one) the equivalent mask bits Both values must be positive integers.
     *
     * @param bitValue the bit value to test
     * @param mask the mask to put over the bit value
     * @return true if logical AND returns a positive value
     */
    public static boolean contain(int bitValue, int mask) {
        if(bitValue < 0 || mask < 0) {
            return (false);
        }
        return (bitValue & mask) > 0;
    }

    /**
     * Static method to turn on the bitValue bits that are on in the onBits. All other bits remain the same Both values must be positive
     * integers.
     * <p>
     * Truth Table <br>
     * bitValue onBits result <br>
     * 1 1 1<br>
     * 1 0 1<br>
     * 0 1 1<br>
     * 0 0 0<br>
     * </p>
     *
     * @param bitValue the bit value to change
     * @param onBits the bits to turn on
     * @return the result of turning the bits on
     */
    public static int turnOn(int bitValue, int onBits) {
        if(bitValue < 0 || onBits < 0) {
            return (-1);
        }
        return (bitValue | onBits);
    }

    /**
     * Static method to turn off the bitValue bits that are on in the offBits. All other bits remain the same Both values must be positive
     * integers.
     * <p>
     * Truth Table <br>
     * bitValue offBits result <br>
     * 1 1 0<br>
     * 1 0 1<br>
     * 0 1 0<br>
     * 0 0 0<br>
     * </p>
     *
     * @param bitValue the bit value to change
     * @param offBits the bits to turn off
     * @return the result of turning the bits on
     */
    public static int turnOff(int bitValue, int offBits) {
        if(bitValue < 0 || offBits < 0) {
            return (-1);
        }
        return (bitValue & ~offBits);
    }

    /**
     * returns a representation of the integer bits as a binary string separated into their four, eight bit clusters
     *
     * @param value the value to be represented
     * @return a string representation of the integer bits
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    public static String getStringBinaryForBits(int value) {
        int displayMask = 1 << 31;
        StringBuilder buf = new StringBuilder(35);

        for(int c = 1; c <= 32; c++) {
            buf.append((value & displayMask) == 0 ? '0' : '1');
            value <<= 1;
            if(c % 8 == 0) {
                buf.append(' ');
            }
        }
        return buf.toString();
    }

    /**
     * These should be overridden an call {@code getStringList(int bits, Class<?> cls)}
     * this is a reflections class that requires the {@code Class<?>} to work
     *
     * @param bits the bits to breakdown
     * @return a list of the String names
     */
    public static List<String> getStringFromBits(int bits) {
        return Stream.of("UNDEFINED").collect(Collectors.toList());
    }

    /**
     * This should be overridden an call {@code getBitsFromString(String stringList, Class<?> cls)}
     * this is a reflections class that requires the {@code Class<?>} to work
     *
     * @param sBits the String to convert
     * @return the Bit value of Strings found
     */
    public static int getBitsFromString(String sBits) {
        return UNDEFINED;
    }

    protected static int getBitsFromString(String stringList, Class<?> cls) {
        int rtnBits = 0;
        try {
            while(cls != null) {
                for(Field field : cls.getDeclaredFields()) {
                    if(Modifier.isPublic(field.getModifiers())
                                        && Modifier.isStatic(field.getModifiers())
                                        && Modifier.isFinal(field.getModifiers())
                                        && field.getType().isPrimitive()) {
                        // add the bits
                        field.setAccessible(true);
                        Object fieldValue = field.get(null);
                        if(fieldValue instanceof Integer && stringList.contains(field.getName())) {
                            rtnBits |= (Integer) fieldValue;
                        }
                    }
                }
                cls = cls.getSuperclass();
                if(cls.equals(AbstractBits.class)) {
                    cls = null;
                }
            }
        } catch(SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return UNDEFINED;
        }
        return (rtnBits);
    }

    protected static List<String> getStringFromBits(int bits, Class<?> cls) {
        List<String> rtnList = new LinkedList<>();
        try {
            while(cls != null) {
                for(Field field : cls.getDeclaredFields()) {
                    if(Modifier.isPublic(field.getModifiers())
                                        && Modifier.isStatic(field.getModifiers())
                                        && Modifier.isFinal(field.getModifiers())
                                        && field.getType().isPrimitive()) {
                        // get the value and check
                        field.setAccessible(true);
                        Object fieldValue = field.get(null);
                        if(fieldValue instanceof Integer) {
                            int value = (Integer) fieldValue;
                            // check value is power of 2
                            if((value & -value) == value && contain(bits, value)) {
                                rtnList.add(field.getName());
                            }
                        }
                    }
                }
                cls = cls.getSuperclass();
                if(cls.equals(AbstractBits.class)) {
                    cls = null;
                }
            }
        } catch(SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            rtnList.add("UNDEFINED");
        }
        return rtnList;
    }

}
