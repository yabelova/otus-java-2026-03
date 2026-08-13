package ru.otus;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.security.InMemoryUserDao;
import ru.otus.security.UserAuthService;
import ru.otus.security.UserAuthServiceImpl;
import ru.otus.server.ClientsWebServer;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Server test should ")
class ClientsWebServerTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String TEMPLATES_DIR = "/templates/";

    private static ClientsWebServer webServer;
    private static InMemoryClientDao clientDao;

    private HttpClient http;

    @BeforeAll
    static void setUp() throws Exception {
        clientDao = new InMemoryClientDao();
        UserAuthService authService = new UserAuthServiceImpl(new InMemoryUserDao());
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        webServer = new ClientsWebServer(WEB_SERVER_PORT, clientDao, templateProcessor, authService);
        webServer.start();
    }

    @BeforeEach
    void setUpHttp() {
        clientDao.clear();
        http = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
    }

    @AfterAll
    static void tearDown() throws Exception {
        if (webServer != null) {
            webServer.stop();
        }
    }

    @Test
    @DisplayName("redirect anonymous GET Clients request to the login page")
    void shouldRedirectAnonymousGetClientsToLogin() throws Exception {
        HttpResponse<String> response = get("clients");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(location(response)).endsWith("/login");
    }

    @Test
    @DisplayName("redirect anonymous POST Clients request to the login page")
    void shouldRedirectAnonymousPostClientsToLogin() throws Exception {
        HttpResponse<String> response = post("clients", "name=Yoda");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(location(response)).endsWith("/login");
    }

    @Test
    @DisplayName("show login form on GET /login")
    void shouldShowLoginForm() throws Exception {
        HttpResponse<String> response = get("login");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).contains("name=\"login\"", "name=\"password\"", "Sign in");
    }

    @Test
    @DisplayName("show clients list and create form to admin after login")
    void shouldShowClientsListForAdmin() throws Exception {
        clientDao.save(new Client("Harry Potter", new Address("Privet Drive"), List.of(new Phone("+44 (0) 1483 559 845"))));

        login("admin", "admin");
        HttpResponse<String> response = get("clients");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).contains("Harry Potter", "Privet Drive", "+44 (0) 1483 559 845", "Create client");
    }

    @Test
    @DisplayName("show clients list to simple user without create form")
    void shouldShowClientsListForSimpleUser() throws Exception {
        clientDao.save(new Client("Sherlock Holmes", new Address("Baker Street"), List.of(new Phone("+44 (0) 20 7224 3688"))));

        login("user", "user");
        HttpResponse<String> response = get("clients");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).contains("Sherlock Holmes", "Baker Street", "+44 (0) 20 7224 3688");
        assertThat(response.body()).doesNotContain("Create client");
    }

    @Test
    @DisplayName("show error message on invalid credentials")
    void shouldShowLoginErrorOnInvalidCredentials() throws Exception {
        HttpResponse<String> response = login("admin", "wrong-password");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).contains("Invalid login or password");
    }

    @Test
    @DisplayName("create a new client on POST from admin")
    void shouldCreateClientForAdmin() throws Exception {
        login("admin", "admin");

        String body = "name=" + urlEncode("Tony Stark")
                + "&address=" + urlEncode("Malibu Point")
                + "&phones=" + urlEncode("+1 (212) 970-4133, +1 (212) 555-0199");
        HttpResponse<String> response = post("clients", body);

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(location(response)).endsWith("/clients");

        assertThat(clientDao.getClients()).hasSize(1);
        Client saved = clientDao.getClients().getFirst();
        assertThat(saved.getName()).isEqualTo("Tony Stark");
        assertThat(saved.getAddress().getStreet()).isEqualTo("Malibu Point");
        assertThat(saved.getPhones()).extracting(Phone::getNumber).containsExactly("+1 (212) 970-4133", "+1 (212) 555-0199");
    }

    @Test
    @DisplayName("redirect and not create a client on POST from simple user")
    void shouldNotCreateClientForSimpleUser() throws Exception {
        login("user", "user");

        HttpResponse<String> response = post("clients", "name=Thor");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(location(response)).endsWith("/clients");
        assertThat(clientDao.getClients()).isEmpty();
    }

    @Test
    @DisplayName("redirect to login page after logout")
    void shouldRedirectToLoginAfterLogout() throws Exception {
        login("admin", "admin");

        HttpResponse<String> logoutResponse = get("logout");
        assertThat(logoutResponse.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(location(logoutResponse)).endsWith("/login");

        HttpResponse<String> response = get("clients");
        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(location(response)).endsWith("/login");
    }

    @Test
    @DisplayName("redirect to login page on logout without a session")
    void shouldRedirectToLoginOnLogoutWithoutSession() throws Exception {
        HttpResponse<String> response = get("logout");

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(location(response)).endsWith("/login");
    }

    private HttpResponse<String> get(String path) throws Exception {
        return http.send(HttpRequest.newBuilder()
                .uri(URI.create(WEB_SERVER_URL + path))
                .GET()
                .build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String formBody) throws Exception {
        return http.send(HttpRequest.newBuilder()
                .uri(URI.create(WEB_SERVER_URL + path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formBody))
                .build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> login(String login, String password) throws Exception {
        String body = "login=" + urlEncode(login) + "&password=" + urlEncode(password);
        return post("login", body);
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String location(HttpResponse<?> response) {
        return response.headers().firstValue("Location").orElse("");
    }
}
