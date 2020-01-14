package com.paxar.qps.common.exception.handler;

import com.paxar.qps.common.exception.RequestHandlerNotFoundException;
import com.paxar.qps.common.web.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class RequestHandlerNotFoundExceptionHandler implements ExceptionHandler<RequestHandlerNotFoundException> {

    private final Logger logger;

    public RequestHandlerNotFoundExceptionHandler(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(final RequestHandlerNotFoundException ex, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        logger.error("Failed to found request handler to process request", ex);
    }
}
