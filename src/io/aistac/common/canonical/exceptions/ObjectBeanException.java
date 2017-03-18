/**
 * @(#)PersistenceException.java
 *
 * Copyright:	Copyright (c) 2009
 * Company:	Oathouse.com Ltd
 */

package io.aistac.common.canonical.exceptions;

/**
 * The {@code PersistenceException} Class
 *
 * @author 	Darryl Oatridge
 * @version 	1.00 29-Jun-2009
 */
public class ObjectBeanException extends OathouseException {
    private static final long serialVersionUID = -7592630156355006762L;

    /**
     * Constructs an instance of <code>MessageQueueException</code> with the specified detail message.
     * The severity level is set to medium, application error.
     * @param msg the detail message.
     */
    public ObjectBeanException(String msg) {
        super(SEVERITY.MEDIUM, msg);
    }

    /**
     * Constructs an instance of <code>MessageQueueException</code> with the specified detail message and a severity:
     * application threatening (high severity), application error (medium severity) or application warning (low severity).
     * @param severity the severity level of the Exception
     * @param msg the detail message.
     */
    public ObjectBeanException(SEVERITY severity, String msg) {
        super(severity, msg);
    }


}
