package ru.otus.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {

    private final long id;
    private final String name;
    private final String login;
    private final String password;
    private final Role role;
}
