package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.security.UserAuthService;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"java:S1989"})
public class LoginServlet extends HttpServlet {

    private static final String TEMPLATE_LOGIN = "login.html";
    private static final String REDIRECT_CLIENTS = "/clients";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_SESSION_USER = "user";
    private static final String LOGIN_ERROR_MESSAGE = "Invalid login or password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private final transient TemplateProcessor templateProcessor;
    private final transient UserAuthService authService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(TEMPLATE_LOGIN, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        var authUser = authService.authenticate(login, password);
        if (authUser.isPresent()) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            session.setAttribute(ATTR_SESSION_USER, authUser.get());
            response.sendRedirect(REDIRECT_CLIENTS);
        } else {
            response.setContentType("text/html");
            response.getWriter().println(templateProcessor.getPage(
                    TEMPLATE_LOGIN, Map.of(ATTR_ERROR, LOGIN_ERROR_MESSAGE)));
        }
    }
}
