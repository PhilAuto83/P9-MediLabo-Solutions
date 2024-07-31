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
                logger.error("Le nombre maximum de session ouverte est fixé à 1");
                response.sendRedirect("login?sessionError");
            }else{
                if(request.getUserPrincipal() != null){
                    logger.error("Le login ou le mot de passe est incorrect pour l'utilisateur {}",request.getUserPrincipal().getName() );
                }else{
                    logger.error("Le login ou le mot de passe est incorrect ou l'utilisateur n'est pas enregistré dans l'application.");
                }
                response.sendRedirect("login?error");
            }

        }
}

