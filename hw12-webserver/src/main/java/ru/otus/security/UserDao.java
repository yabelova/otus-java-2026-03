package ru.otus.security;

import java.util.Optional;

public interface UserDao {

    Optional<User> findByLogin(String login);
}
