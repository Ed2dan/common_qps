package com.paxar.qps.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Validator for checking tha data of the request before {@link RequestHandler} will work.
 */
public interface RequestValidator {

    boolean validate(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse);
}
