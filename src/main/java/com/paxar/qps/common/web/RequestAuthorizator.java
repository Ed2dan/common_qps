package com.paxar.qps.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Authorizator for checking tha data of the request before {@link RequestHandler} will work.
 */
public interface RequestAuthorizator {

    boolean authorize(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse);
}
