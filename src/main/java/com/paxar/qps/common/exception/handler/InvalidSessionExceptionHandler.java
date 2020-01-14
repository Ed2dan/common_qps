package com.paxar.qps.common.exception.handler;

import com.paxar.qps.common.exception.InvalidSessionException;
import com.paxar.qps.common.web.ExceptionHandler;
import com.paxar.qps.common.web.QPSWebUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class InvalidSessionExceptionHandler implements ExceptionHandler<InvalidSessionException> {

    private final Logger logger;

    public InvalidSessionExceptionHandler(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(final InvalidSessionException ex, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        logger.error("Failed to process request due to invalid session", ex);

        response.sendRedirect(QPSWebUtils.URL_LOGIN_PAGE);
    }
}
