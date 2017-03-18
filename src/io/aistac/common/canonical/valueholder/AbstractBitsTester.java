/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * @(#)AbstractBitsTester.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.valueholder;

import io.aistac.common.canonical.exceptions.OathouseException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.reflect.ConstructorUtils;

/**
 * The {@code AbstractBitsTester} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 27-Mar-2016
 */
public class AbstractBitsTester {

    public static void testBits(String fullClsName) throws OathouseException {
        AbstractBitsTester.testBits(fullClsName, new LinkedList<>());
    }

    public static void testBits(String fullClsName, List<String> exemptBitNames) throws OathouseException {
        // add exempts that are in AbstractBits class
        exemptBitNames.addAll(Stream.of("UNDEFINED","NO_VALUE","ALL_ON").collect(Collectors.toList()));
        // check the instance
        checkInstance(getClass(fullClsName));
        // checxk the fields and values
        checkBits(getClass(fullClsName), exemptBitNames);
    }

    private static Class<?> getClass(String fullClsName) throws OathouseException {
        Class<?> cls = null;
        try {
            cls = Class.forName(fullClsName);
        } catch(ClassNotFoundException classNotFoundException) {
            throw new OathouseException(fullClsName + " does not exist. Class not found!");
        }
        assertNotNull(fullClsName + ".forName() returned null", cls);
        return (cls);
    }

    private static void checkInstance(Class<?> cls) throws OathouseException {
        String simpleClsName = cls.getSimpleName();
        Object obj = null;
        try {
            obj = ConstructorUtils.invokeConstructor(cls);
        } catch(Exception ex) {
            fail(cls.getName() + " failed to initialise with blank constructor, " + ex.getClass().getSimpleName());
        }
        assertNotNull(simpleClsName + " initalisation returned null", obj);
        assertTrue(cls.getName() + " does not extend AbstractBits", obj instanceof AbstractBits);
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    private static void checkBits(Class<?> cls, List<String> exempt) throws OathouseException {
        String simpleClsName = cls.getSimpleName();
        Set<Integer> checkSet = new TreeSet<>();
        while(cls != null) {
            for(Field field : cls.getDeclaredFields()) {
                if(!exempt.contains(field.getName())) {
                    SimpleEntry<Integer, String> checkField = checkField(field, simpleClsName);
                    if(checkSet.contains(checkField.getKey())) {
                        fail(checkField.getValue() + " duplicates value [" + checkField.getKey() + "]");
                    }
                    checkSet.add(checkField.getKey());
                }
            }
            cls = cls.getSuperclass();
            if(cls.equals(Object.class)) {
                cls = null;
            }
        }
    }

    private static SimpleEntry<Integer,String> checkField(Field field, String simpleClsName) throws OathouseException {
        StringBuilder sb = new StringBuilder();
        sb.append(simpleClsName);
        sb.append(" [");
        sb.append(field.getName());
        sb.append("]");

        if(!Modifier.isPublic(field.getModifiers())) {
            fail(sb.append(" was not public").toString());
        }
        if(!Modifier.isStatic(field.getModifiers())) {
            fail(sb.append(" was not static").toString());
        }
        if(!Modifier.isFinal(field.getModifiers())) {
            fail(sb.append(" was not final").toString());
        }
        if(!field.getType().isPrimitive()) {
            fail(sb.append(" is not of type primative").toString());
        }
        try {
            field.setAccessible(true);
            Object fieldValue = field.get(null);
            if(!(fieldValue instanceof Integer)){
                fail(sb.append(" is not an integer primative").toString());
            }
            int value = (Integer) fieldValue;
            if(value < 0) {
                fail(sb.append(" is a negative value").toString());
            }
            if((value & -value) != value) {
                fail(sb.append(" is not a power of 2 [").append(value).append("]").toString());
            }
            return new SimpleEntry<>(value, sb.toString());
        } catch(Exception ex) {
            throw new OathouseException(ex.getMessage());
        }
    }

    /* **************************
     * methods to mirror Junit
     * **************************/
    static private void fail(String message) throws OathouseException {
        throw new OathouseException(message == null ? "" : message);
    }

    static private void assertEquals(String message, Object expected, Object actual) throws OathouseException {
        if(expected == null && actual == null) {
            return;
        }
        if(expected != null && expected.equals(actual)) {
            return;
        }
        fail(message + " - Equals Failed: Expected " + expected.toString() + " but was " + actual.toString());
    }

    static private void assertTrue(String message, boolean condition) throws OathouseException {
        if(!condition) {
            fail(message);
        }
    }

    static private void assertFalse(String message, boolean condition) throws OathouseException {
        assertTrue(message, !condition);
    }

    static private void assertNotNull(String message, Object object) throws OathouseException {
        assertTrue(message, object != null);
    }

    static private void assertNotSame(String message, Object unexpected, Object actual) throws OathouseException {
        if(unexpected.equals(actual)) {
            fail(message + " - Expected " + unexpected + " to NOT equal " + actual);
        }
    }

    private AbstractBitsTester() {
    }

}
