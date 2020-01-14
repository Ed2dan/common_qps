package com.paxar.qps.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler of any {@link Throwable} that might be thrown while a {@link RequestHandler} works.
 */
public interface ExceptionHandler<T extends Throwable> {

    void handle(T ex, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
