package ru.erminson.lc.repository;

import ru.erminson.lc.model.dto.request.ProfileRequest;
import ru.erminson.lc.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPassword(String login, String password);
    List<User> findAll();
    int edit(String login, ProfileRequest profileRequest);
}
