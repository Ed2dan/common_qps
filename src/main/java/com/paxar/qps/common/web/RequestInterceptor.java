package com.paxar.qps.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestInterceptor {

    /**
     * Intercept the execution of a handler. Called after HandlerMapping determined
     * an appropriate handler object, but before HandlerAdapter invokes the handler.
     * <p>Front controller processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can decide to abort the execution chain,
     * typically sending an HTTP error or writing a custom response.
     * <p>The default implementation returns {@code true}.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute
     * @return {@code true} if the execution chain should proceed with the
     *         next interceptor or the handler itself.
     * @throws Exception in case of errors
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, RequestHandler handler)
            throws Exception {
        return true;
    }

    /**
     * Intercept the execution of a handler.
     * <p>Front controller processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can post-process an execution,
     * getting applied in inverse order of the execution chain (made so to comply with Spring implementation).
     * <p>The default implementation is empty.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler handler that started execution
     * @throws Exception in case of errors
     */
    default void postHandle(HttpServletRequest request, HttpServletResponse response, RequestHandler handler)
            throws Exception {
    }
}
