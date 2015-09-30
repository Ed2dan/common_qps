package com.paxar.qps.common.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.Validate;

import com.averydennison.data.Specstar;
import com.paxar.d2comm.TokenUpdate;
import com.paxar.soap.SpecStarUserSoapStub;

/**
 * Utility class that contains all main QPS constants (like pages, servlet init parameters, etc..) and common methods.
 * @author rsav
 * @version 1.0
 *
 */
public final class QPSWebUtils {
    
    /**
     * Key that is used for storing server base init parameter.
     */
    public static final String SERVER_BASE_INIT_PARAM = "ServerBase";
    
    /**
     * Key that is used for storing DB host init parameter.
     */
    public static final String DB_HOST_INIT_PARAM = "DBHost";
    
    /**
     * Key that is used for storing SMTP host init parameter.
     */
    public static final String SMTP_HOST_INIT_PARAM = "SMTPHost";

    /**
     * Parameter that uses for storing user name in session, request parameter, etc...
     */
    public static final String USER_PARAMETER = "user";
    
    /**
     * Parameter that uses for storing token value in session, request parameter, etc...
     */
    public static final String TOKEN_PARAMETER = "token";
    
    /**
     * URL that should be used for redirecting requests if login is necessary (for example, if session is not valid). 
     */
    public static final String URL_LOGIN_PAGE = "https://www.webservices.averydennison.com";
    
    /**
     * Request parameter that specifies action id of received request,
     */
    public static final String REQUEST_PARAMETER_ACTION_ID = "actionId";

    private QPSWebUtils() {}
    
    /**
     * Extracts user name from session.
     * @param request, not null
     * @return User name, or null, if it was not found in session
     * @throws IllegalArgumentException if request is null
     */
    public static final String getUserFromRequest(HttpServletRequest request) {
        Validate.notNull(request);
        
        return (String) request.getSession().getAttribute(USER_PARAMETER);
    }
    
    /**
     * Validates session. Tries to access or setup user name and if failures - throws exception.
     * @param request not null
     * @throws IllegalArgumentException if request is null
     * @throws InvalidSessionException if failed to setup or access user name
     */
    public static final void validateSession(HttpServletRequest request) throws InvalidSessionException {
        Validate.notNull(request);
        
        HttpSession session = request.getSession();
        if (session == null) {
            throw new InvalidSessionException();
        }
        
        if (session.getAttribute(USER_PARAMETER) == null) {
            session.setAttribute(USER_PARAMETER, request.getParameter(USER_PARAMETER));
        }
                
        String token = request.getParameter(TOKEN_PARAMETER) == null ? 
                (String) session.getAttribute(TOKEN_PARAMETER) : request.getParameter(TOKEN_PARAMETER);
        if (Specstar.isValidToken(token)) {
            session.setAttribute(TOKEN_PARAMETER, token);
            String user = new SpecStarUserSoapStub().getUserDetail(token, 8); // 8 = retailer
            if (user != null) {
                session.setAttribute(USER_PARAMETER, user);
            }
            new TokenUpdate().refreshToken(token);
        }
        if (session.getAttribute(USER_PARAMETER) == null) {
            throw new InvalidSessionException();
        }
    }
    
    /**
     * Performs redirect action to URL that is response for scheduling batches to user.
     * User name is retrieved from reuest's session attributes.
     * @param request http request
     * @param response http response
     * @throws IllegalArgumentException if user name was not found in request's attributes
     * @throws IOException if exception was occurred during redirecting request
     */
    public static final void redirectToCustomerUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String user = getUserFromRequest(request);
        
        Validate.notEmpty(user);
        
        response.sendRedirect(String.format("/d2comm/customer_update.jsp?user=%s", user));
    }

}
