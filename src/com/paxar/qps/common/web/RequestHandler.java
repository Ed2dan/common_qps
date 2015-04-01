package com.paxar.qps.common.web;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for classes that will be responsible for handling all requests.
 * @author rsav
 * @version 1.0
 *
 */
public interface RequestHandler {

    /**
     * <p>Method that should be used for handling request.</p>
     * <p>
     *  <strong>Note: </strong>
     *  All implementations of this method should throw ServletException and IOException only in case, 
     *  when it was caused by using request or response arguments 
     *  (like {@link HttpServletResponse#sendRedirect(String)}, {@link RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)}, etc.).
     *  In all other cases (if, for example, {@link FileNotFoundException} was thrown during executing some business logic that is not associated with request and response directly),
     *  these exceptions should be handled by handle method implementation.
     * </p>
     * @param request
     * @param response
     * @throws ServletException if some exception occurred during working with request or response argument 
     * @throws IOException if some exception occurred during working with request or response argument
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException;

}
