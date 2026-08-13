package ru.otus;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import ru.otus.dao.ClientDao;
import ru.otus.dao.ClientDaoHibernate;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.security.InMemoryUserDao;
import ru.otus.security.UserAuthService;
import ru.otus.security.UserAuthServiceImpl;
import ru.otus.server.ClientsWebServer;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import static ru.otus.util.HibernateUtils.buildSessionFactory;

@Slf4j
public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        try (SessionFactory sessionFactory = buildSessionFactory(Client.class, Address.class, Phone.class)) {
            ClientDao clientDao = new ClientDaoHibernate(sessionFactory);

            UserAuthService authService = new UserAuthServiceImpl(new InMemoryUserDao());
            TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

            ClientsWebServer webServer = new ClientsWebServer(WEB_SERVER_PORT, clientDao, templateProcessor, authService);
            webServer.start();
            webServer.join();
        }
    }
}
