package ru.otus.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("java:S2068")
public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "Administrator", "admin", "admin", Role.ADMIN));
        users.put(2L, new User(2L, "User", "user", "user", Role.USER));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream().filter(v -> v.getLogin().equals(login)).findFirst();
    }
}
