package com.paxar.qps.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Authorizer for checking the data of the request before {@link RequestHandler} will work.
 */
public interface RequestAuthorizer {

    boolean authorize(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse);

    void onDenied(final HttpServletRequest req, final HttpServletResponse resp);
}
