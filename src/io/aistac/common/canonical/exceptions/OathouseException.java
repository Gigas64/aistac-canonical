/**
 * @(#)OathouseException.java
 *
 * Copyright:	Copyright (c) 2009
 * Company:     Oathouse.com Ltd
 */

package io.aistac.common.canonical.exceptions;

/**
 * The {@code OathouseException} Class is the super class of all oathouse
 * exceptions
 *
 * @author      Darryl Oatridge
 * @version 	1.00 29-Jun-2009
 */
public class OathouseException extends Exception {
    private static final long serialVersionUID = -7208081664891963383L;
    private final SEVERITY severity;

    /**
     * The Severity of the exception indicating if the exception is application threatening (high severity),
     * application error (medium severity) or application warning (low severity) exception.
     */
    @SuppressWarnings("ProtectedInnerClass")
    public static enum SEVERITY {
        HIGH, MEDIUM, LOW;
    }

    /**
     * Constructs an instance of <code>OathouseException</code> with the specified detail message.
     * The severity level is set to medium, application error.
     * @param msg the detail message.
     */
    public OathouseException(String msg) {
        super(msg);
        this.severity = SEVERITY.MEDIUM;
    }

    /**
     * Constructs an instance of <code>OathouseException</code> with the specified detail message and a severity:
     * application threatening (high severity), application error (medium severity) or application warning (low severity).
     * @param severity the severity level of the Exception
     * @param msg the detail message.
     */
    public OathouseException(SEVERITY severity, String msg) {
        super(msg);
        this.severity = severity;
    }

    /**
     * Used to test if this is threatening exception
     * @return boolean, true if HIGH severity
     */
    public boolean isThreatening() {
        return severity.equals(SEVERITY.HIGH);
    }

    /**
     * Used to test if this a warning exception
     * @return boolean, true if HIGH severity
     */
    public boolean isWarning() {
        return severity.equals(SEVERITY.LOW);
    }

    /**
     * Used to retried the severity of the exception
     * @return the severity level
     */
    public SEVERITY getSeverity() {
        return severity;
    }
}
