package ru.otus.security;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    @Override
    public Optional<User> authenticate(String login, String password) {
        return userDao.findByLogin(login)
                .filter(user -> user.getPassword().equals(password));
    }
}
