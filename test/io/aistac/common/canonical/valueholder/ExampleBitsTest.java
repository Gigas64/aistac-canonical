/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.aistac.common.canonical.valueholder;

import io.aistac.common.canonical.valueholder.AbstractBitsTester;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class ExampleBitsTest {

    @Test
    public void testExampleBitsClass() throws Exception {
        String clsName = ExampleFailBits.class.getName();
        List<String> exemptNames = Stream.of
            ("NOT_STATIC"
            ,"NOT_FINAL"
            ,"NOT_INT"
            ,"DUPLICATE"
            ,"NEGATIVE"
            ,"MIX")
        .collect(Collectors.toList());
        // do the test
        AbstractBitsTester.testBits(clsName, exemptNames);
    }

    /**
     * Unit test: Test the getStringBinaryForBits
     */
    @Test
    public void unit01_getStringBinaryForBits() {
        assertEquals("00000000 00000000 00000000 00000000 ", ExampleBits.getStringBinaryForBits(ExampleBits.ALL_OFF));
        assertEquals("10000000 00000000 00000000 00000000 ", ExampleBits.getStringBinaryForBits(ExampleBits.UNDEFINED));
        assertEquals("01111111 11111111 11111111 11111111 ", ExampleBits.getStringBinaryForBits(ExampleBits.ALL_ON));
        assertEquals("00000000 00000000 00000000 00000001 ", ExampleBits.getStringBinaryForBits(ExampleBits.FIRST));
        assertEquals("00000000 00000000 00000000 00000010 ", ExampleBits.getStringBinaryForBits(ExampleBits.SECOND));
        assertEquals("00000000 00000000 00000000 00000100 ", ExampleBits.getStringBinaryForBits(ExampleBits.THIRD));
        assertEquals("00000000 00000000 10000000 00000000 ", ExampleBits.getStringBinaryForBits(ExampleBits.MID));
        assertEquals("01000000 00000000 00000000 00000000 ", ExampleBits.getStringBinaryForBits(ExampleBits.LAST));
        assertEquals("00000000 00000000 00000000 00000011 ", ExampleBits.getStringBinaryForBits(ExampleBits.SECOND | ExampleBits.FIRST | ExampleBits.FIRST));
        assertEquals("01000000 00000000 10000000 00000001 ", ExampleBits.getStringBinaryForBits(ExampleBits.MIX));
        assertEquals("01000000 00000000 10000000 00000001 ", ExampleBits.getStringBinaryForBits(ExampleBits.MIX | ExampleBits.MIX));
        assertEquals("11000000 00000000 10000000 00000001 ", ExampleBits.getStringBinaryForBits(ExampleBits.MIX | ExampleBits.UNDEFINED));

    }

    /**
     * Unit test: tests the abstract isBits() method
     */
    @Test
    public void unit02_isBits() throws Exception {
        int bits = 0;
        int mask = 0;
        assertFalse(ExampleBits.contain(bits, mask));
        bits = ExampleBits.FIRST | ExampleBits.SECOND;
        mask = ExampleBits.FIRST;
        assertTrue(ExampleBits.contain(bits, mask));
        assertTrue(ExampleBits.match(bits, mask));
        assertFalse(ExampleBits.equals(bits, mask));
        mask |= ExampleBits.SECOND;
        assertTrue(ExampleBits.contain(bits, mask));
        assertTrue(ExampleBits.match(bits, mask));
        assertTrue(ExampleBits.equals(bits, mask));

        bits = ExampleBits.FIRST | ExampleBits.SECOND;
        mask = ExampleBits.THIRD;
        assertFalse(ExampleBits.contain(bits, mask));
        assertFalse(ExampleBits.match(bits, mask));
        assertFalse(ExampleBits.equals(bits, mask));

        mask = ExampleBits.THIRD | ExampleBits.FIRST | ExampleBits.SECOND;
        assertTrue(ExampleBits.contain(bits, mask));
        assertFalse(ExampleBits.match(bits, mask));
        assertFalse(ExampleBits.equals(bits, mask));
    }

    /**
     * Unit test: turnOn
     */
    @Test
    public void unit03_turnOn() throws Exception {
        int bits = 3;
        int turnOn = 6;
        int result = 7;
        assertEquals(result, ExampleBits.turnOn(bits, turnOn));

    }

    /**
     * Unit test: turnOn
     */
    @Test
    public void unit03_turnOff() throws Exception {
        int bits = 1;
        int turnOff = 2;
        int result = 1;
        assertEquals(result, ExampleBits.turnOff(bits, turnOff));
        bits = 3;
        turnOff = 6;
        result = 1;
        assertEquals(result, ExampleBits.turnOff(bits, turnOff));

    }

    /*
     *
     */
    @Test
    public void testStringList() throws Exception {

        String result = ExampleBits.getStringFromBits(ExampleBits.MIX).toString();
        String test = "[FIRST, MID, LAST]";
        assertThat(result,is(equalTo(test)));

        assertThat(ExampleBits.getBitsFromString(test),is(ExampleBits.MIX));
    }


}
