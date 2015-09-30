package com.paxar.qps.common.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.Validate;

/**
 * This common request handler is responsible for forwarding requests by request dispatcher path 
 * that was specified during initialization (see {@link #path}). 
 * @author rsav
 *
 */
public final class ForwardRequestHandler implements RequestHandler {
    
    /**
     * Request dispatcher path 
     */
    private String path;
    
    public ForwardRequestHandler(String path) {
        Validate.notEmpty(path);
        this.path = path;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

}
