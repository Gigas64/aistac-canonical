/*
 * @(#)AbstractBitsTester.java
 *
 * Copyright:	Copyright (c) 2010
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.valueholder;

import io.aistac.common.canonical.valueholder.AbstractBits;
import static io.aistac.common.canonical.valueholder.AbstractBits.UNDEFINED;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code AbstractBitsTester} Class match a bitwise helper to provides a numerical
 * logic values for comparison of periodSd
 * <p>
 * Logic follows the following bitwise logic <br>
 *  &amp; - true if both operands are true, otherwise false <br>
 *  ^ - true if both operands are different, otherwise false <br>
 *  | - false if both operands are false, otherwise, true <br>
 *</p>
 *
 * <p>
 * An example of how to build a bitmask for  might be:
 * </p>
 *
 * <blockquote>
 * <pre>
 *     int bitflagMask = BTypeFlag.ATTENDING + BTypeFlag.PRECHARGE;
 * </pre>
 * </blockquote>
 *
 * @author Darryl Oatridge
 * @version 1.00 16-Nov-2011
 */
public class ExampleBits extends AbstractBits {
    public static final int FIRST  = (int) Math.pow(0x2, 0x0);
    public static final int SECOND = (int) Math.pow(0x2, 0x1);
    public static final int THIRD  = (int) Math.pow(0x2, 0x2);
    public static final int MID    = (int) Math.pow(0x2, 0x0f);
    public static final int LAST   = (int) Math.pow(0x2, 0x1e);

    public static final int MIX = FIRST | MID | LAST;


    /**
     * returns a list of Constant names found in the bits
     *
     * @param bits the bits to breakdown
     * @return a list of the String names
     */
    public static List<String> getStringFromBits(int bits) {
        Class<ExampleBits> cls = ExampleBits.class;
        return AbstractBits.getStringFromBits(bits, cls);
}

    /**
     * returns a bit value for named constance found in the given String
     *
     * @param sBits the String to convert
     * @return the Bit value of Strings found
     */
    public static int getBitsFromString(String sBits) {
        Class<ExampleBits> cls = ExampleBits.class;
        return AbstractBits.getBitsFromString(sBits, cls);
    }

}
