/*
 * @(#)AbstractBitsTester.java
 *
 */
package io.aistac.common.canonical.valueholder;

import io.aistac.common.canonical.valueholder.AbstractBits;

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
public class ExampleFailBits extends AbstractBits {
    public final int NOT_STATIC = 0;
    public static int NOT_FINAL = 0;
    public static final long NOT_INT = 0;
    public static final int FIRST  = (int) Math.pow(0x2, 0x0);
    public static final int SECOND = (int) Math.pow(0x2, 0x1);
    public static final int DUPLICATE = (int) Math.pow(0x2, 0x1);
    public static final int NEGATIVE = (int) -Math.pow(0x2, 0x1);

    public static final int MIX = FIRST | SECOND;


}
