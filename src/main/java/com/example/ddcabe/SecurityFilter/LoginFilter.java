package com.example.ddcabe.SecurityFilter;


import com.example.ddcabe.Service.JWTAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;


@Component
public class LoginFilter extends OncePerRequestFilter {
    private final JWTAuthenticationService authenticationService;

    @Autowired
    public LoginFilter(JWTAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    private static final Logger logger = Logger.getLogger(LoginFilter.class.getName());

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        //We have 4 class to handle, Stock, User, OperatorStock and Session
        //We only allow login api from User class, the rest will be blocked for authorization

        //Extract the API path and their method
        String path = request.getRequestURI();

        //We have login is the allowed method, as it will return JWT to the front end
        return "/users/login".equals(path);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        logger.info("inside doFilterInternal");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        //Get the token from the header request
        String headerToken = authenticationService.extractTokenStringFromHeader(request);
        if (headerToken == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing JWT Token");
            return;
        }
        //Validate token
        try {
            if (!authenticationService.isValidToken(headerToken)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT NOT VALIDATED");
                return;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        logger.info(" --> destroy() method is invoked");
    }
}