package ru.otus.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ClientFormMapper should ")
class ClientFormMapperTest {

    private final ClientFormMapper mapper = new ClientFormMapper();

    @Test
    @DisplayName("parse phones splitting by comma and trimming")
    void shouldParsePhones() {
        assertThat(mapper.parsePhones(" +1 (212) 970-4133 , +1 (212) 555-0199 "))
                .extracting(Phone::getNumber)
                .containsExactly("+1 (212) 970-4133", "+1 (212) 555-0199");
    }

    @Test
    @DisplayName("ignore empty phone entries")
    void shouldIgnoreEmptyPhoneEntries() {
        assertThat(mapper.parsePhones("+1 (212) 970-4133,,  "))
                .extracting(Phone::getNumber)
                .containsExactly("+1 (212) 970-4133");
    }

    @Test
    @DisplayName("return an empty set for null or blank phones")
    void shouldReturnEmptySetForNullOrBlankPhones() {
        assertThat(mapper.parsePhones(null)).isEmpty();
        assertThat(mapper.parsePhones("   ")).isEmpty();
    }

    @Test
    @DisplayName("return null for null or blank address")
    void shouldReturnNullForNullOrBlankAddress() {
        assertThat(mapper.parseAddress(null)).isNull();
        assertThat(mapper.parseAddress("   ")).isNull();
    }

    @Test
    @DisplayName("build an Address for a non-blank address")
    void shouldBuildAddress() {
        assertThat(mapper.parseAddress("Privet Drive").getStreet()).isEqualTo("Privet Drive");
    }

    @Test
    @DisplayName("trim the client name")
    void shouldTrimClientName() {
        assertThat(mapper.toClient("   Yoda   ", null, null).getName()).isEqualTo("Yoda");
    }

    @Test
    @DisplayName("reject a blank client name")
    void shouldRejectBlankName() {
        assertThatThrownBy(() -> mapper.toClient("   ", null, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("must not be blank");
    }

    @Test
    @DisplayName("build a Client from form fields")
    void shouldBuildClient() {
        Client client = mapper.toClient("Harry Potter", "Privet Drive", "+44 (0) 1483 559 845");
        assertThat(client.getName()).isEqualTo("Harry Potter");
        assertThat(client.getAddress().getStreet()).isEqualTo("Privet Drive");
        assertThat(client.getPhones()).extracting(Phone::getNumber)
                .containsExactly("+44 (0) 1483 559 845");
    }
}
