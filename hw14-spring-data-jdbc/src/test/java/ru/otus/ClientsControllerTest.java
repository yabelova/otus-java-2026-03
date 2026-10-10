package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.ClientRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("ClientsController should ")
@AutoConfigureMockMvc
class ClientsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository repository;

    @Test
    @DisplayName("redirect GET / to the clients page")
    void shouldRedirectRootToClients() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));
    }

    @Test
    @DisplayName("render the clients page with an empty list and the create form")
    void shouldRenderEmptyClientsPage() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(content().string(containsString("Clients list")))
                .andExpect(content().string(containsString("Create client")));
    }

    @Test
    @DisplayName("render saved clients with name, address and phones")
    void shouldRenderSavedClients() throws Exception {
        repository.save(new Client("Harry Potter", new Address("Privet Drive"),
                Set.of(new Phone("+44 (0) 1483 559 845"))));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Harry Potter")))
                .andExpect(content().string(containsString("Privet Drive")))
                .andExpect(content().string(containsString("+44 (0) 1483 559 845")));
    }

    @Test
    @DisplayName("create a client from the form and redirect to the clients page")
    void shouldCreateClient() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("name", "Tony Stark")
                        .param("address", "Malibu Point")
                        .param("phones", "+1 (212) 970-4133, +1 (212) 555-0199,, "))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        var clients = repository.findAll();
        assertThat(clients).hasSize(1);
        Client saved = clients.getFirst();
        assertThat(saved.getName()).isEqualTo("Tony Stark");
        assertThat(saved.getAddress().getStreet()).isEqualTo("Malibu Point");
        assertThat(saved.getPhones()).extracting(Phone::getNumber)
                .containsExactlyInAnyOrder("+1 (212) 970-4133", "+1 (212) 555-0199");
    }

    @Test
    @DisplayName("create a client with only a name when address and phones are absent")
    void shouldCreateClientWithoutAddressAndPhones() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("name", "Yoda"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        var clients = repository.findAll();
        assertThat(clients).hasSize(1);
        Client saved = clients.getFirst();
        assertThat(saved.getName()).isEqualTo("Yoda");
        assertThat(saved.getAddress()).isNull();
        assertThat(saved.getPhones()).isEmpty();
        assertThat(count("address")).isZero();
        assertThat(count("phone")).isZero();
    }

    @Test
    @DisplayName("not store blank address and phones")
    void shouldNotStoreBlankAddressAndPhones() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("name", "Yoda")
                        .param("address", "   ")
                        .param("phones", "  "))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        var clients = repository.findAll();
        assertThat(clients).hasSize(1);
        Client saved = clients.getFirst();
        assertThat(saved.getAddress()).isNull();
        assertThat(saved.getPhones()).isEmpty();
        assertThat(count("address")).isZero();
        assertThat(count("phone")).isZero();
    }

    @Test
    @DisplayName("reject a create request without the name parameter")
    void shouldRejectCreateWithoutName() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("address", "Asgard")
                        .param("phones", "+1 (212) 970-4133"))
                .andExpect(status().isBadRequest());
    }

    private int count(String table) {
        return jdbc.queryForObject("SELECT count(*) FROM " + table, Integer.class);
    }
}
