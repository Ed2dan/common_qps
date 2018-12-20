package com.paxar.qps.common.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    private Map<String, RequestHandler> requestHandlers = new HashMap<>();

    /**
     * <p>Map with all authorizers that will be provided by factory.</p>
     * <p>Contains action id as key, and corresponding request security instance as value.</p>
     */
    private Map<String, RequestAuthorizer> requestAuthorizers = new HashMap<>();

    /**
     * <p>Default request handler that will be used if system tries to get request handler for empty action id.</p>
     * <p>Can be null.</p>
     */
    private RequestHandler defaultRequestHandler;

    /**
     * <p>Default request security that will be used if system tries to get request security for empty action id.</p>
     * <p>Can be null.</p>
     */
    private RequestAuthorizer defaultRequestAuthorizer;

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
    public RequestHandler getRequestHandler(HttpServletRequest request)
            throws RequestHandlerNotFoundException {
        return getRequestHandler(request.getParameter(QPSWebUtils.REQUEST_PARAMETER_ACTION_ID));
    }

    /**
     * <p>Looks for {@link RequestAuthorizer} implementation that might be used for authorization specified request.</p>
     * <p>
     *  Extracts action id parameter from request by key {@link QPSWebUtils#REQUEST_PARAMETER_ACTION_ID}
     *  and uses {@link #getRequestAuthorizer(String)} with this value in order to found necessary request security.
     *  @param request
     * </p>
     */
    public Optional<RequestAuthorizer> getRequestAuthorizer(HttpServletRequest request) {
        return getRequestAuthorizer(request.getParameter(QPSWebUtils.REQUEST_PARAMETER_ACTION_ID));
    }

    /**
     * <p>Lookups for request handler with specified action id</p>
     * <p>If action id is empty but factory contains default request handler (see {@link #defaultRequestHandler}) - it returns default request handler instance.</p>
     * @param actionId id that will be used for looking up for necessary request handler
     * @return Request handler that corresponds to specified action id
     * @throws RequestHandlerNotFoundException if factory does not contains request handler for some action id,
     *  or action id is empty, but factory does not contain default request handler
     */
    public RequestHandler getRequestHandler(String actionId)
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
     * <p>Lookups for request security with specified action id</p>
     * <p>If action id is empty but factory contains default request security (see {@link #defaultRequestAuthorizer}) - it returns default request security instance.</p>
     * @param actionId id that will be used for looking up for necessary request security
     * @return Request security that corresponds to specified action id
     */
    public Optional<RequestAuthorizer> getRequestAuthorizer(String actionId) {
        if (StringUtils.isEmpty(actionId) || !this.requestAuthorizers.containsKey(actionId)) {
            return getDefaultRequestAuthorizer();
        }
        return Optional.ofNullable(this.requestAuthorizers.get(actionId));
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

        this.requestHandlers.put(actionId, requestHandler);
        return new ValidationBinder(actionId);
    }

    /**
     * <p>Puts request authorizer with specified action id to list of request authorizers.</p>
     * <p><strong>Note: </strong>if factory already contains request authorizer with such action id - it will be overridden with new authorizer.</p>
     * @param actionId, not empty
     * @param requestAuthorizer, not null
     * @return Current request handler factory instance
     * @throws IllegalArgumentException if action id is empty, or request authorizer is null
     */
    public RequestHandlerFactory putRequestAuthorizer(final String actionId,
            final RequestAuthorizer requestAuthorizer) {
        Validate.notEmpty(actionId);
        Validate.notNull(requestAuthorizer);

        this.requestAuthorizers.put(actionId, requestAuthorizer);
        return this;
    }

    /**
     * <p>Makes specified request handler instance as default in this factory (see {@link #defaultRequestHandler}).</p>
     * <p><strong>Note: </strong>if factory already contains default request handler - it will be overridden.</p>
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

    /**
     * <p>Makes specified request security instance as default in this factory (see {@link #defaultRequestAuthorizer}).</p>
     * <p><strong>Note: </strong>if factory already contains default request security - it will be overridden.</p>
     * @param requestAuthorizer, not null
     * @return Current request handler factory instance
     * @throws IllegalArgumentException if request handler is null
     */
    public RequestHandlerFactory putDefaultRequestAuthorizer(final RequestAuthorizer requestAuthorizer) {
        Validate.notNull(requestAuthorizer);

        this.defaultRequestAuthorizer = requestAuthorizer;
        return this;
    }

    private RequestHandler getDefaultRequestHandler() throws RequestHandlerNotFoundException {
        if (this.defaultRequestHandler == null) {
            throw new RequestHandlerNotFoundException("Action id is empty and factory does not contain default request handler");
        }
        return this.defaultRequestHandler;
    }

    private Optional<RequestAuthorizer> getDefaultRequestAuthorizer() {
        return Optional.ofNullable(this.defaultRequestAuthorizer);
    }

    public final class ValidationBinder {

        private final String currentActionId;

        private ValidationBinder(final String currentActionId) {
            this.currentActionId = currentActionId;
        }

        public RequestHandlerFactory withAuthorizer(final RequestAuthorizer authorizer) {
            if (authorizer != null) {
                RequestHandlerFactory.this.requestAuthorizers.put(currentActionId, authorizer);
            }
            return RequestHandlerFactory.this;
        }

        public ValidationBinder putRequestHandler(final String actionId, final RequestHandler requestHandler) {
            return RequestHandlerFactory.this.putRequestHandler(actionId, requestHandler);
        }

        /**
         * <p>This method is not required to be called. It is just to have returning possibility to {@link RequestHandlerFactory} object without setting a {@link RequestAuthorizer}.</p>
         */
        public RequestHandlerFactory finish() {
            return RequestHandlerFactory.this;
        }
    }
}
