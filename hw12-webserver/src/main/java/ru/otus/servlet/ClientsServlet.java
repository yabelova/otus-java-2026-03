package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.dao.ClientDao;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.security.Role;
import ru.otus.security.User;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"java:S1989"})
public class ClientsServlet extends HttpServlet {

    private static final String TEMPLATE_CLIENTS = "clients.html";
    private static final String REDIRECT_CLIENTS = "/clients";
    private static final String ATTR_CLIENTS = "clients";
    private static final String ATTR_IS_ADMIN = "isAdmin";
    private static final String ATTR_SESSION_USER = "user";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONES = "phones";
    private static final String PHONES_DELIMITER = ",";

    private final transient TemplateProcessor templateProcessor;
    private final transient ClientDao clientDao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(ATTR_SESSION_USER);
        boolean isAdmin = user != null && user.getRole() == Role.ADMIN;
        List<Client> clients = clientDao.findAll();
        Map<String, Object> params = Map.of(ATTR_CLIENTS, clients, ATTR_IS_ADMIN, isAdmin);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(TEMPLATE_CLIENTS, params));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(ATTR_SESSION_USER);
        if (user == null || user.getRole() != Role.ADMIN) {
            response.sendRedirect(REDIRECT_CLIENTS);
            return;
        }

        String name = request.getParameter(PARAM_NAME);
        String street = request.getParameter(PARAM_ADDRESS);
        List<Phone> phones = Arrays.stream(request.getParameter(PARAM_PHONES).split(PHONES_DELIMITER))
                .map(String::trim)
                .filter(phone -> !phone.isEmpty())
                .map(Phone::new)
                .toList();
        Client client = new Client(name, new Address(street), phones);
        clientDao.save(client);
        response.sendRedirect(REDIRECT_CLIENTS);
    }
}
