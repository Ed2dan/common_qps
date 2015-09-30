package com.paxar.qps.common.web;

/**
 * Throws when user name was not found in session.
 * @author rsav
 * @version 1.0
 *
 */
public class InvalidSessionException extends Exception{

    private static final long serialVersionUID = 8710968111180810370L;
    
    InvalidSessionException() {
        super("Http session is not valid because it does not contain user name.");
    }

}
