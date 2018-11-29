package com.paxar.qps.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action that should be performed if authorizer returns <code>false</code>.
 */
public interface OnAuthorizerFailedListener {

    void onFail(final HttpServletRequest req, final HttpServletResponse resp);
}
