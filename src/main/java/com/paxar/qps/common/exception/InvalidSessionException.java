package com.paxar.qps.common.exception;

/**
 * Throws when user name was not found in session.
 * @author rsav
 * @version 1.0
 *
 */
public class InvalidSessionException extends Exception{

    private static final long serialVersionUID = 4042469345225130312L;

    public InvalidSessionException() {
        super("Http session is not valid because it does not contain user name.");
    }
}
