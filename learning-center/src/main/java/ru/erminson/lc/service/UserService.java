package ru.erminson.lc.service;

import ru.erminson.lc.model.dto.request.ProfileRequest;
import ru.erminson.lc.model.entity.User;

import java.util.List;

public interface UserService {
    User findByLogin(String login);
    List<User> findAll();
    void edit(String login, ProfileRequest profileRequest);
}
