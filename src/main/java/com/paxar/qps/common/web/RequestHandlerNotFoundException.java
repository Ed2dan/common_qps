package com.paxar.qps.common.web;

/**
 * Throws when it was not found request handler (see {@link RequestHandler}) for some request.
 * @author rsav
 * @version 1.0
 *
 */
public class RequestHandlerNotFoundException extends Exception {

    private static final long serialVersionUID = -2229029266985694893L;

    public RequestHandlerNotFoundException() {
        super();
    }

    public RequestHandlerNotFoundException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RequestHandlerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHandlerNotFoundException(String message) {
        super(message);
    }

    public RequestHandlerNotFoundException(Throwable cause) {
        super(cause);
    }

}
