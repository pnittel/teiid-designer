/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.jdbc.relational.impl;

import org.teiid.core.TeiidRuntimeException;

/**
 * UserCancelledException
 *
 * @since 8.0
 */
public class UserCancelledException extends TeiidRuntimeException {

    /**
     */
    private static final long serialVersionUID = 1L;
    private static final String msg = org.teiid.designer.jdbc.relational.ModelerJdbcRelationalConstants.Util.getString("UserCancelledException.User_cancelled_operation"); //$NON-NLS-1$

    /**
     * Construct an instance of UserCancelledException.
     * 
     */
    public UserCancelledException() {
        super(msg);
    }

    /**
     * Construct an instance of UserCancelledException.
     * @param message
     */
    public UserCancelledException(String message) {
        super(message);
    }

    /**
     * Construct an instance of UserCancelledException.
     * @param code
     * @param message
     */
    public UserCancelledException(int code, String message) {
        super(Integer.toString(code), message);
    }

    /**
     * Construct an instance of UserCancelledException.
     * @param e
     */
    public UserCancelledException(Throwable e) {
        super(e);
    }

    /**
     * Construct an instance of UserCancelledException.
     * @param e
     * @param message
     */
    public UserCancelledException(Throwable e, String message) {
        super(e, message);
    }

    /**
     * Construct an instance of UserCancelledException.
     * @param e
     * @param code
     * @param message
     */
    public UserCancelledException(Throwable e, int code, String message) {
        super(e, Integer.toString(code), message);
    }

}
