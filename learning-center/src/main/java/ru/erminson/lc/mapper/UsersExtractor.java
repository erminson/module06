package ru.erminson.lc.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.erminson.lc.model.Role;
import ru.erminson.lc.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class UsersExtractor implements ResultSetExtractor<List<User>> {
    private static final String ID_COLUMN = "ID";
    private static final String LOGIN_COLUMN = "LOGIN";
    private static final String PASSWORD_COLUMN = "PASSWORD";
    private static final String ENABLED_COLUMN = "ENABLED";
    private static final String USER_NAME_COLUMN = "USER_NAME";
    private static final String CREATED_AT_COLUMN = "CREATED_AT";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private static final String ROLE_NAME_COLUMN = "ROLE_NAME";

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, User> map = new HashMap<>();
        while (rs.next()) {
            long userId = rs.getLong(ID_COLUMN);
            User user = map.get(userId);
            if (user == null) {
                String login = rs.getString(LOGIN_COLUMN);
                String password = rs.getString(PASSWORD_COLUMN);
                boolean enabled = rs.getInt(ENABLED_COLUMN) == 1;
                String name = rs.getString(USER_NAME_COLUMN);
                LocalDateTime createdAt = rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime();

                user = new User(userId, login, password, enabled, name, createdAt, new HashSet<>());
                map.put(userId, user);
            }

            long roleId = rs.getLong(ROLE_ID_COLUMN);
            if (roleId > 0) {
                String roleName = rs.getString(ROLE_NAME_COLUMN);
                Role role = new Role(roleId, roleName);
                user.addRole(role);
            }
        }

        return new ArrayList<>(map.values());
    }
}
