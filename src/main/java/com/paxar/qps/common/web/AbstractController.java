package com.paxar.qps.common.web;

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

    private static final long serialVersionUID = -906865592156025442L;

    /**
     * Contains all request handlers that are supported by controller implementation.
     */
    private RequestHandlerFactory requestHandlerFactory = new RequestHandlerFactory();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            setupRequestHandlerFactory(this.requestHandlerFactory);
        } catch (RequestHandlerFactorySetupException e) {
            getLogger().error("Failed to setup request handler factory due to unexpected exception", e);
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
        setNoCahce(response);
        if (!preProcess(request, response)) {
            return;
        }
        try {
            if (!validateAndAuthorize(request, response)) {
                return;
            }

            final RequestHandler handler = this.requestHandlerFactory.getRequestHandler(request);

            handler.handle(request, response);
        } catch (InvalidSessionException e) {
            getLogger().error("Failed to process request due to invalid session", e);
            onInvalidSessionException(request, response, e);
        } catch (RequestHandlerNotFoundException e) {
            getLogger().error("Failed to found request handler to process request", e);
            onRequestHandlerNotFoundException(request, response, e);
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
                    final boolean granted = auth.authorize(request, response);

                    if (!granted) {
                        auth.onDenied(request, response);
                    }
                    return granted;
                })
                .orElseThrow(() -> new IllegalStateException("There should be an Authorizer for [actionId] = '"
                        + actionId + "'. You can put DefaultAuthorizer there."));
    }

    private void setNoCahce(HttpServletResponse response) {
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

    /**
     * <p>Handles situation when session is not valid.</p>
     * <p>Default method implementation forwards user to login page.</p>
     *
     * @param e - InvalidSessionException that was thrown
     * @throws ServletException if some ServletException occurred during working with or response arguments
     * @throws IOException if some ServletException occurred during working with or response arguments
     */
    protected void onInvalidSessionException(HttpServletRequest request, HttpServletResponse response,
            InvalidSessionException e) throws ServletException, IOException {
        response.sendRedirect(QPSWebUtils.URL_LOGIN_PAGE);
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
     * <p>Invokes when controller can't find necessary request handler for processing request.</p>
     * <p><strong>Note:</strong> abstract controller already logged RequestHandlerNotFoundException exception.</p>
     *
     * @param e - RequestHandlerNotFoundException that was thrown during looking for request handler
     * @throws ServletException if some ServletException occurred during working with or response arguments
     * @throws IOException if some IOException occurred during working with or response arguments
     */
    protected abstract void onRequestHandlerNotFoundException(HttpServletRequest request, HttpServletResponse response,
            RequestHandlerNotFoundException e) throws ServletException, IOException;

}
