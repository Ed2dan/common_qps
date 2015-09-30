package com.paxar.qps.common.web;

/**
 * Throws when it was failed to setup request handler factory in some Controller (see {@link AbstractController#setupRequestHandlerFactory(RequestHandlerByActionFactory)}).
 * @author rsav
 * @version 1.0
 *
 */
public class RequestHandlerFactorySetupException extends Exception {

    private static final long serialVersionUID = -111956521804579886L;

    public RequestHandlerFactorySetupException() {
        super();
    }

    public RequestHandlerFactorySetupException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RequestHandlerFactorySetupException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHandlerFactorySetupException(String message) {
        super(message);
    }

    public RequestHandlerFactorySetupException(Throwable cause) {
        super(cause);
    }

}
