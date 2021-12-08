package ru.erminson.lc.service;

import ru.erminson.lc.model.User;

import java.util.List;

public interface UserService {
    User findByLogin(String login);
    List<User> findAll();
}
