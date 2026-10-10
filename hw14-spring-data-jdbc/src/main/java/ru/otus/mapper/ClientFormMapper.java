package ru.otus.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClientFormMapper {

    public Client toClient(String name, String address, String phones) {
        return new Client(requireName(name), parseAddress(address), parsePhones(phones));
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client name must not be blank");
        }
        return name.trim();
    }

    Set<Phone> parsePhones(String phones) {
        if (phones == null) {
            return Set.of();
        }
        return Arrays.stream(phones.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Phone::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    Address parseAddress(String address) {
        if (address == null || address.isBlank()) {
            return null;
        }
        return new Address(address);
    }
}
