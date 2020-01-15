package com.paxar.qps.common.web;

import com.paxar.qps.common.exception.InvalidSessionException;
import com.paxar.qps.common.exception.RequestHandlerFactorySetupException;
import com.paxar.qps.common.exception.RequestHandlerNotFoundException;
import com.paxar.qps.common.exception.handler.InvalidSessionExceptionHandler;
import com.paxar.qps.common.exception.handler.RequestHandlerNotFoundExceptionHandler;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * <p>Provides abstraction for those classes that will be used as Controllers for some RBO batch builders.</p>
 * <p>The following logic is used for handling requests:</p>
 * <p>
 * For handling each request systems uses implementations of {@link RequestHandler} interface.
 * In order to mark some specific action to corresponding request handler implementation systems uses
 * {@link RequestHandlerFactory}.
 * </p>
 * <p>
 * In short flow of handling requests is next:
 * <ul>
 * <li>Controller forwarded received request to {@link #doGetPost(HttpServletRequest, HttpServletResponse)} method.</li>
 * <li>Validates session and if it is not valid - redirected user to login page.</li>
 * <li>
 * If session is valid - tries to found request handler in request handlers factory (see
 * {@link #requestHandlerFactory}) and if found -
 * forwarded control to received instance of handler.
 * </li>
 * <li>If request handler was not found - invokes request handler not found exception.</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Note:</strong>
 * in order to use this abstraction user should implement methods that will setting up request handler factory
 * and respond on situations, when request handler was not found for some request.
 * </p>
 *
 * @author rsav
 * @version 1.0
 */
public abstract class AbstractController extends HttpServlet {

    private static final long serialVersionUID = -6897160557762711931L;

    /**
     * Contains all request handlers that are supported by controller implementation.
     */
    private RequestHandlerFactory requestHandlerFactory = new RequestHandlerFactory();

    /**
     * Contains all exception handlers that are supported by controller implementation.
     */
    private ExceptionHandlerFactory exceptionHandlerFactory = new ExceptionHandlerFactory();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            setupRequestHandlerFactory(this.requestHandlerFactory);
            setupRequiredExceptionHandlerFactory(this.exceptionHandlerFactory);
        } catch (RequestHandlerFactorySetupException ex) {
            getLogger().error("Failed to setup request handler factory due to unexpected exception", ex);
            throw new ServletException(
                    "Failed to init controller due to unexpected exception during reuqest handler factory setup");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGetPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGetPost(req, resp);
    }

    /**
     * <p>Handles GET and POST requests.</p>
     * <p>Validates session and if it is not valid - redirects user to login page (see {@link #LOGIN_PAGE_URL})</p>
     * <p>
     * I session is valid tries to get request handler for request in request handler factory (see
     * {@link #requestHandlerFactory}).
     * If found - uses it to process request, otherwise -
     * calls
     * {@link #onRequestHandlerNotFoundException(HttpServletRequest, HttpServletResponse, RequestHandlerNotFoundException)} method.
     * </p>
     * <p>It also calls set of methods before and after each action. Below is this list</p>
     * <ul>
     * <li>{@link #preProcess(HttpServletRequest, HttpServletResponse)}</li>
     * <li>{@link #preValidation(HttpServletRequest, HttpServletResponse)}</li>
     * <li>{@link #postValidation(HttpServletRequest, HttpServletResponse)}</li>
     * <li>{@link #postProcess(HttpServletRequest, HttpServletResponse)}</li>
     * </ul>
     *
     * @throws ServletException if some ServletException occurred during working with or response arguments
     * @throws IOException if some IOException occurred during working with or response arguments
     */
    protected void doGetPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setNoCache(response);
        if (!preProcess(request, response)) {
            return;
        }

        try {
            if (!validateAndAuthorize(request, response)) {
                return;
            }

            final RequestHandler handler = this.requestHandlerFactory.getRequestHandler(request);

            handler.handle(request, response);
        } catch (Exception ex) {
            handleException(ex, request, response);
        }
        postProcess(request, response);
    }

    private boolean validateAndAuthorize(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, InvalidSessionException {
        if (!preValidation(request, response)) {
            return false;
        }
        QPSWebUtils.validateSession(request);

        if (!postValidation(request, response)) {
            return false;
        }

        final String actionId = request.getParameter(QPSWebUtils.REQUEST_PARAMETER_ACTION_ID);

        return this.requestHandlerFactory.getRequestAuthorizer(actionId)
                .map(auth -> {
                    final boolean isAccessGranted = auth.authorize(request, response);

                    if (!isAccessGranted) {
                        auth.onAccessDenied(request, response);
                    }
                    return isAccessGranted;
                })
                .orElseThrow(() -> new IllegalStateException("There should be an Authorizer for [actionId] = '"
                        + actionId + "'. You can put DefaultAuthorizer there."));
    }

    private void setNoCache(HttpServletResponse response) {
        response.setHeader("X-UA-Compatible", "IE=edge");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
    }

    /**
     * This method will be executed before each request processing (Also before validation)..
     *
     * @return indicator if abstract controller should continue processing request. Default is true
     */
    protected boolean preProcess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        return true;
    }

    /**
     * This method will be executed after each request processing.
     *
     * @return indicator if abstract controller should continue processing request. Default is true
     */
    protected void postProcess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * This method will be executed before each request validation action.
     *
     * @return indicator if abstract controller should continue processing request. Default is true
     */
    protected boolean preValidation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        return true;
    }

    /**
     * This method will be executed before each request validation action.
     *
     * @return indicator if abstract controller should continue processing request. Default is true
     */
    protected boolean postValidation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        return true;
    }

    protected void handleException(final Throwable ex, final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
            exceptionHandlerFactory.getExceptionHandler(ex, request).handle(ex, request, response);
        } catch (final Throwable exp) {
            try {
                getDefaultExceptionHandler().handle(exp, request, response);
            } catch (final Exception exp1) {
                getLogger().error(exp1);
            }
        }
    }

    /**
     * Setups exception handler factory that was transferred in argument with all necessary exception handlers.
     */
    protected final void setupRequiredExceptionHandlerFactory(final ExceptionHandlerFactory factory) {
        factory.putDefaultExceptionHandler(getDefaultExceptionHandler());
        factory.putExceptionHandler(InvalidSessionException.class, getInvalidSessionExceptionHandlerHandler());
        factory.putExceptionHandler(RequestHandlerNotFoundException.class, getRequestHandlerNotFoundExceptionHandler());
        setupExceptionHandlerFactory(factory);
    }

    protected ExceptionHandler<RequestHandlerNotFoundException> getRequestHandlerNotFoundExceptionHandler() {
        return new RequestHandlerNotFoundExceptionHandler(getLogger());
    }

    protected ExceptionHandler<InvalidSessionException> getInvalidSessionExceptionHandlerHandler() {
        return new InvalidSessionExceptionHandler(getLogger());
    }

    /**
     * Returns instance of logger that will be used for logging main actions.
     *
     * @return Logger instance
     */
    protected abstract Logger getLogger();

    /**
     * Setups request handler factory that was transferred in argument with all necessary request handlers.
     *
     * @throws RequestHandlerFactorySetupException if or was failed to setup factory
     */
    protected abstract void setupRequestHandlerFactory(RequestHandlerFactory factory)
            throws RequestHandlerFactorySetupException;

    /**
     * Setups exception handler factory that was transferred in argument with all necessary exception handlers.
     */
    protected abstract void setupExceptionHandlerFactory(ExceptionHandlerFactory factory);

    /**
     * Returns {@link ExceptionHandler} instance that will be used as default.
     *
     * @return ExceptionHandler<Throwable> instance
     */
    protected abstract ExceptionHandler<Throwable> getDefaultExceptionHandler();
}
