package ru.otus.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.ClientRepository;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ClientsController {
    private final ClientRepository repository;

    @GetMapping("/")
    public String root() { return "redirect:/clients"; }

    @GetMapping("/clients")
    public String list(Model model) {
        model.addAttribute("clients", repository.findAll());
        return "clients";
    }

    @PostMapping("/clients")
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String address,
                         @RequestParam(required = false) String phones) {
        Set<Phone> phoneSet = phones == null ? Set.of()
                : Arrays.stream(phones.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Phone::new)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
        Address addr = (address == null || address.isBlank()) ? null : new Address(address);
        repository.save(new Client(name, addr, phoneSet));
        return "redirect:/clients";
    }
}
