package com.paxar.qps.common.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.Validate;

/**
 * <p>This factory provides an {@link ExceptionHandler} implementation basing on the action id parameter from the
 * request and the {@link Throwable} subtype.</p>
 * <p>It also provides a common exception handler for the exception class if it's not specified per action id
 * parameter from the request.</p>
 * <p>Also, it provides a default exception handler in case it's not specified per action id
 * parameter from the request and the common exception handler for the Exception class is not defined.</p>
 */
public class ExceptionHandlerFactory {

    /**
     * A default {@link ExceptionHandler} instance for any type of {@link Throwable}.
     */
    private ExceptionHandler<Throwable> defaultHandler;

    /**
     * <p>Map with all handlers that will be provided by factory.</p>
     * <p>Contains {@link Throwable} instance class as key and corresponding {@link ActionExceptionHandlerFactory}
     * instance as value.</p>
     */
    private Map<Class<? extends Throwable>, ActionExceptionHandlerFactory> exceptionHandlers
            = new HashMap<>();

    /**
     * <p>Looks for {@link ExceptionHandler} implementation that might be used for handling specified exception.</p>
     */
    public <T extends Throwable> ExceptionHandler<T> getExceptionHandler(final T ex) {
        return getExceptionHandler(ex, (String) null);
    }

    /**
     * <p>Looks for {@link ExceptionHandler} implementation that might be used for handling specified exception and
     * request.</p>
     * <p>
     * Extracts action id parameter from request by key {@link QPSWebUtils#REQUEST_PARAMETER_ACTION_ID}
     * and uses {@link #getExceptionHandler(Throwable, String)} with this value in order to found necessary exception
     * handler.
     *
     * @param request </p>
     */
    public <T extends Throwable> ExceptionHandler<T> getExceptionHandler(final T ex, final HttpServletRequest request) {
        return getExceptionHandler(ex, request.getParameter(QPSWebUtils.REQUEST_PARAMETER_ACTION_ID));
    }

    /**
     * <p>Lookups for exception handler for a {@link Throwable} subtype with specified action id.</p>
     * <p>If the {@link Throwable} subtype is empty then it returns default {@link ExceptionHandler} instance.</p>
     *
     * @param ex that will be used for looking up for necessary exception handler
     * @param actionId that will be used for looking up for necessary exception handler
     * @return Exception handler that corresponds to specified action id and the {@link Throwable} subtype
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> ExceptionHandler<T> getExceptionHandler(final T ex, final String actionId) {
        return Optional.ofNullable(findActionExceptionHandlerFactory(ex))
                .map(factory -> factory.getExceptionHandler(actionId))
                .orElse(getDefaultExceptionHandler());
    }

    public <T extends Throwable> ActionExceptionHandlerFactory<T> putExceptionHandler(final Class<T> exClass,
            final ExceptionHandler<T> exHandler) {
        return putExceptionHandler(exClass, null, exHandler);
    }

    /**
     * <p>Puts {@link ExceptionHandler} with specified action id to @{@link #exceptionHandlers}.</p>
     * <p><strong>Note:</strong> if factory already contains {@link ExceptionHandler} with such the action id
     * then it will be overridden with new handler.</p>
     *
     * @param actionId, not empty
     * @param exHandler, not null
     * @return Current exception handler factory instance
     * @throws NullPointerException if actionId or exHandler is null
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> ActionExceptionHandlerFactory<T> putExceptionHandler(final Class<T> exClass,
            final String actionId, final ExceptionHandler<T> exHandler) {
        Validate.notNull(exClass);
        Validate.notNull(exHandler);

        if (actionId != null) {
            return exceptionHandlers.putIfAbsent(exClass, new ActionExceptionHandlerFactory<T>()
                    .putDefaultActionExceptionHandler(exHandler))
                    .putActionExceptionHandler(actionId, exHandler);
        } else {
            return exceptionHandlers.put(exClass, new ActionExceptionHandlerFactory<T>()
                    .putDefaultActionExceptionHandler(exHandler));
        }
    }

    public ExceptionHandlerFactory putDefaultExceptionHandler(final ExceptionHandler<Throwable> exHandler) {
        Validate.notNull(exHandler);

        this.defaultHandler = exHandler;

        return this;
    }

    protected ExceptionHandler getDefaultExceptionHandler() {
        return defaultHandler;
    }

    @SuppressWarnings("unchecked")
    private <T extends Throwable> ActionExceptionHandlerFactory<T> findActionExceptionHandlerFactory(final T ex) {
        if (ex == null) {
            return null;
        }

        Class<? extends Throwable> exClass;

        while (!Throwable.class.equals((exClass = ex.getClass()))) {
            if (exceptionHandlers.containsKey(exClass)) {
                return exceptionHandlers.get(exClass);
            }
        }
        return null;
    }

    /**
     * <p>This factory provides an {@link ExceptionHandler} implementation basing on the action id parameter from the
     * request.</p>
     * <p>It also provides a common exception handler for the exception class if it's not specified per action id
     * parameter from the request.</p>
     */
    public class ActionExceptionHandlerFactory<T extends Throwable> {

        /**
         * A default (common) {@link ExceptionHandler} instance for {@link T}.
         */
        private ExceptionHandler<T> defaultHandler;

        /**
         * <p>Map with all handlers that will be provided by factory.</p>
         * <p>Contains action id as key and corresponding {@link ExceptionHandler} instance as value.</p>
         */
        private Map<String, ExceptionHandler<T>> exceptionHandlers = new HashMap<>();

        private ActionExceptionHandlerFactory() {
        }

        /**
         * <p>Lookups for request handler with specified action id.</p>
         * <p>If action id is empty then it returns default {@link ExceptionHandler} instance.</p>
         *
         * @param actionId id that will be used for looking up for necessary exception handler
         * @return Exception handler that corresponds to specified action id (or default)
         */
        public ExceptionHandler<T> getExceptionHandler(final String actionId) {
            if (actionId == null || !exceptionHandlers.containsKey(actionId)) {
                return defaultHandler;
            }
            return exceptionHandlers.get(actionId);
        }

        /**
         * <p>Puts {@link ExceptionHandler} with specified action id to @{@link #exceptionHandlers}.</p>
         * <p><strong>Note:</strong> if factory already contains {@link ExceptionHandler} with such the action id
         * then it will be overridden with new handler.</p>
         *
         * @param actionId, not empty
         * @param exceptionHandler, not null
         * @return Current exception handler factory instance
         * @throws NullPointerException if actionId or exceptionHandler is null
         * @throws IllegalArgumentException if actionId is empty
         */
        public ActionExceptionHandlerFactory<T> putActionExceptionHandler(final String actionId,
                final ExceptionHandler<T> exceptionHandler) {
            Validate.notEmpty(actionId);
            Validate.notNull(exceptionHandler);

            exceptionHandlers.put(actionId, exceptionHandler);
            return this;
        }

        @SuppressWarnings("unchecked")
        public ActionExceptionHandlerFactory<T> putDefaultActionExceptionHandler(final ExceptionHandler<T> exHandler) {
            Validate.notNull(exHandler);

            this.defaultHandler = exHandler;

            return this;
        }

        public ExceptionHandlerFactory done() {
            return ExceptionHandlerFactory.this;
        }
    }
}
