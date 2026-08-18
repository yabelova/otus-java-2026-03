package ru.otus.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import ru.otus.dao.ClientDao;
import ru.otus.security.AuthorizationFilter;
import ru.otus.security.UserAuthService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.ClientsServlet;
import ru.otus.servlet.LoginServlet;
import ru.otus.servlet.LogoutServlet;

@Slf4j
@RequiredArgsConstructor
public class ClientsWebServer {

    private final int port;
    private final ClientDao clientDao;
    private final TemplateProcessor templateProcessor;
    private final UserAuthService authService;

    private Server server;

    public void start() throws Exception {
        if (server == null) {
            server = createServer();
        }
        server.start();
    }

    public void join() throws Exception {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    private Server createServer() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/");
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, clientDao)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
        servletContextHandler.addFilter(new FilterHolder(new AuthorizationFilter()), "/clients", null);

        Server server = new Server(port);
        server.setHandler(servletContextHandler);
        return server;
    }
}
