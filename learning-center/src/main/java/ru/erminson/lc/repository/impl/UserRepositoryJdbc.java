package ru.erminson.lc.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.erminson.lc.mapper.UserExtractor;
import ru.erminson.lc.mapper.UsersExtractor;
import ru.erminson.lc.model.dto.request.ProfileRequest;
import ru.erminson.lc.model.entity.User;
import ru.erminson.lc.repository.UserRepository;
import ru.erminson.lc.utils.SqlFactory;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepositoryJdbc implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserExtractor userExtractor;
    private SqlFactory sqlFactory;

    public UserRepositoryJdbc(JdbcTemplate jdbcTemplate, UserExtractor userExtractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.userExtractor = userExtractor;
    }

    @Autowired
    public void setSqlFactory(SqlFactory sqlFactory) {
        this.sqlFactory = sqlFactory;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        throw new UnsupportedOperationException("findByLoginAndPassword(String, String) not supported yet");
    }

    @Override
    public Optional<User> findByLogin(String login) {
        User user = null;
        try {
            user = jdbcTemplate.query(
                    sqlFactory.getSqlQuery("user/select-user-by-login.sql"),
                    userExtractor,
                    login
            );
        } catch (EmptyResultDataAccessException ex) {
            log.debug("User with login: {} wasn't found", login);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        String sql = sqlFactory.getSqlQuery("user/select-all-users.sql");
        return jdbcTemplate.query(sql, new UsersExtractor());
    }

    @Override
    public int edit(String login, ProfileRequest profileRequest) {
        String sql = sqlFactory.getSqlQuery("user/insert-name.sql");
        return jdbcTemplate.update(sql, profileRequest.getName(), login);
    }
}
