package ru.otus.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AuthorizationFilter implements Filter {

    private static final String REDIRECT_LOGIN = "/login";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        log.info("Requested Resource: {}", uri);

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(REDIRECT_LOGIN);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
