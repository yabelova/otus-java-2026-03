package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.mapper.ClientFormMapper;
import ru.otus.repository.ClientRepository;

@Controller
@RequiredArgsConstructor
public class ClientsController {
    private final ClientRepository repository;
    private final ClientFormMapper clientFormMapper;

    @GetMapping("/")
    public String root() {
        return "redirect:/clients";
    }

    @GetMapping("/clients")
    public String list(Model model) {
        model.addAttribute("clients", repository.findAll());
        return "clients";
    }

    @PostMapping("/clients")
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String address,
                         @RequestParam(required = false) String phones) {
        repository.save(clientFormMapper.toClient(name, address, phones));
        return "redirect:/clients";
    }
}
