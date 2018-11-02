package com.paxar.qps.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestValidator {

    boolean validate(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse);
}
