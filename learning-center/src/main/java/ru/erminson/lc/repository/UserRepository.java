package ru.erminson.lc.repository;

import ru.erminson.lc.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPassword(String login, String password);
    List<User> findAll();
}
