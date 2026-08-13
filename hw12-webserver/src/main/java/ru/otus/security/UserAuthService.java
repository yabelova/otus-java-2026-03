package ru.otus.security;

import java.util.Optional;

public interface UserAuthService {

    Optional<User> authenticate(String login, String password);
}
