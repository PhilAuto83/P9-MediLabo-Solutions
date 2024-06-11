package com.phildev.front.mls.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import java.io.IOException;
/***
 * This class is used to redirect users to login page with different error parameters
 * It helps to handle custom errors depending on whether this is a credentials error or session error
 */
public class CustomAuthenticationFailure implements AuthenticationFailureHandler {
        private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailure.class);

        /**
         * This method handles the redirection of users after authentication fails for SessionAuthenticationException or for Invalid input
         * @param request which an {@link HttpServletRequest}
         * @param response which is an {@link HttpServletResponse}
         * @param exception which is an {@link AuthenticationException}
         */
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
            if(exception instanceof SessionAuthenticationException){
                logger.error("Session is no longer valid due to maximum session set to 1 for current user");
                response.sendRedirect("login?sessionError");
            }else{
                if(request.getUserPrincipal() != null){
                    logger.error("Invalid credentials typed by user so authentication fails for user {}",request.getUserPrincipal().getName() );
                }else{
                    logger.error("Invalid credentials typed or unknown user in the app.");
                }
                response.sendRedirect("login?error");
            }

        }
}

