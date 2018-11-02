package com.paxar.qps.common.web;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * <p>This factory provides request handler implementation basing on action id parameter from request.</p>
 * <p>Also it provides default request handler if action id parameter was not found in request.</p>
 * @author rsav
 * @version 1.0
 *
 */
public class RequestHandlerFactory {
    
    /**
     * <p>Map with all handlers that will be provided by factory.</p> 
     * <p>Contains action id as key, and corresponding request handler instance as value.</p>
     */
    private Map<String, Map.Entry<RequestHandler, RequestValidator>> requestHandlers = new HashMap<>();

    /**
     * <p>Default request handler that will be used if system tries to get request handler for empty action id.</p>
     * <p>Can be null.</p>
     */
    private RequestHandler defaultRequestHandler;

    /**
     * <p>Default request validator that will be used if system tries to get request validator for empty action id.</p>
     * <p>Can be null.</p>
     */
    private RequestValidator defaultRequestValidator;
    
    /**
     * Default request handler factory constructor.
     */
    public RequestHandlerFactory() {
    }
    
    /**
     * <p>Looks for {@link RequestHandler} implementation that might be used for handling specified request.</p>
     * <p>
     *  Extracts action id parameter from request by key {@link QPSWebUtils#REQUEST_PARAMETER_ACTION_ID}
     *  and uses {@link #getRequestHandler(String)} with this value in order to found necessary request handler.
     *  @param request
     *  @throws RequestHandlerNotFoundException if it was not found necessary implementation of request handler.
     * </p>
     */
    public Map.Entry<RequestHandler, RequestValidator> getRequestHandler(HttpServletRequest request)
            throws RequestHandlerNotFoundException {
        return getRequestHandler(request.getParameter(QPSWebUtils.REQUEST_PARAMETER_ACTION_ID));
    }

    /**
     * <p>Lookups for request handler with specified action id</p>
     * <p>If action id is empty but factory contains default request handler (see {@link #defaultRequestHandler}) - it returns default request handler instance.</p>
     * @param actionId id that will be used for looking up for necessary request handler
     * @return Request handler that corresponds to specified action id
     * @throws RequestHandlerNotFoundException if factory does not contains request handler for some action id,
     *  or action id is empty, but factory does not contain default request handler
     */
    public Map.Entry<RequestHandler, RequestValidator> getRequestHandler(String actionId)
            throws RequestHandlerNotFoundException {
        if (StringUtils.isEmpty(actionId)) {
            return getDefaultRequestHandler();
        }
        if (!this.requestHandlers.containsKey(actionId)) {
            throw new RequestHandlerNotFoundException(String.format("Failed to found request handler for action with id [%s]", actionId));
        }
        
        return this.requestHandlers.get(actionId);
    }
    
    /**
     * <p>Puts request handlers with specified action id to list of request handlers.</p>
     * <p><strong>Note: </strong>if factory already contains request handler with such action id - it will be overridden with new handler.</p>
     * @param actionId, not empty
     * @param requestHandler, not null
     * @return Current request handler factory instance
     * @throws IllegalArgumentException if action id is empty, or request handler is null 
     */
    public ValidationBinder putRequestHandler(String actionId, RequestHandler requestHandler) {
        Validate.notEmpty(actionId);
        Validate.notNull(requestHandler);
        
        this.requestHandlers.put(actionId, new AbstractMap.SimpleEntry<>(requestHandler, defaultRequestValidator));
        return new ValidationBinder(this.requestHandlers.get(actionId));
    }
    
    /**
     * <p>Makes specified request handler instance as default in this factory (see {@link #defaultRequestHandler}).</p>
     * <p><strong>Note: </strong>if factory already contains default request handler - it will be overidden.</p>
     * @param requestHandler, not null
     * @return Current request handler factory instance
     * @throws IllegalArgumentException if request handler is null
     */
    public RequestHandlerFactory putDefaultRequestHandler(RequestHandler requestHandler) {
        Validate.notNull(requestHandler);
        
        this.defaultRequestHandler = requestHandler;
        return this;
    }

    /**
     * <p>Makes specified request validator instance as default in this factory (see {@link #defaultRequestValidator}).</p>
     * <p><strong>Note: </strong>if factory already contains default request validator - it will be overridden.</p>
     * @param requestValidator, not null
     * @return Current request handler factory instance
     * @throws IllegalArgumentException if request validator is null
     */
    public RequestHandlerFactory putDefaultRequestValidator(RequestValidator requestValidator) {
        Validate.notNull(requestValidator);

        this.defaultRequestValidator = requestValidator;
        return this;
    }
    
    /**
     * <p>Appends specified request handler with corresponding action id to current handlers list and makes it default.</p>
     * <p>It calls {@link #putRequestHandler(String, RequestHandler)} and {@link #putDefaultRequestHandler(RequestHandler)} methods with necessary parameters.</p>
     * @param actionId, not empty
     * @return Current request handler factory instance
     * @param requestHandler, not null
     */
    public RequestHandlerFactory putDefaultRequestHandler(String actionId, RequestHandler requestHandler) {
        putRequestHandler(actionId, requestHandler);
        putDefaultRequestHandler(requestHandler);
        
        return this;
    }

    private Map.Entry<RequestHandler, RequestValidator> getDefaultRequestHandler() throws RequestHandlerNotFoundException {
        if (this.defaultRequestHandler == null) {
            throw new RequestHandlerNotFoundException("Action id is empty and factory does not contain default request handler");
        }
        return new AbstractMap.SimpleEntry<>(this.defaultRequestHandler, this.defaultRequestValidator);
    }

    public final class ValidationBinder {

        private final Map.Entry<RequestHandler, RequestValidator> currentHandlerAndValidator;

        private ValidationBinder(final Map.Entry<RequestHandler, RequestValidator> currentHandlerAndValidator) {
            this.currentHandlerAndValidator = currentHandlerAndValidator;
        }

        public RequestHandlerFactory withValidator(final RequestValidator validator) {
            this.currentHandlerAndValidator.setValue(validator);
            return RequestHandlerFactory.this;
        }

        public ValidationBinder putRequestHandler(final String actionId, final RequestHandler requestHandler) {
            return RequestHandlerFactory.this.putRequestHandler(actionId, requestHandler);
        }

        /**
         * <p>This method is not required to be called. It is just to have returning possibility to {@link RequestHandlerFactory} object without setting a {@link RequestValidator}.</p>
         */
        public RequestHandlerFactory finish() {
            return RequestHandlerFactory.this;
        }
    }
}
