package ru.erminson.lc.mapper;

//public class UserRowMapper implements RowMapper<User> {
//    private static final String ID_COLUMN = "ID";
//    private static final String LOGIN_COLUMN = "LOGIN";
//    private static final String PASSWORD_COLUMN = "PASSWORD";
//    private static final String CREATED_AT_COLUMN = "CREATED_AT";
//    private static final String ROLE_NAME_COLUMN = "NAME";
//
//    @Override
//    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//        return new User(
//                rs.getLong(ID_COLUMN),
//                rs.getString(LOGIN_COLUMN),
//                rs.getString(PASSWORD_COLUMN),
//                rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime(),
//                RoleType.valueOf(rs.getString(ROLE_NAME_COLUMN))
//        );
//    }
//}

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.erminson.lc.model.Role;
import ru.erminson.lc.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;

public class UserExtractor implements ResultSetExtractor<User> {
    private static final String ID_COLUMN = "ID";
    private static final String LOGIN_COLUMN = "LOGIN";
    private static final String PASSWORD_COLUMN = "PASSWORD";
    private static final String ENABLED_COLUMN = "ENABLED";
    private static final String USER_NAME_COLUMN = "USER_NAME";
    private static final String CREATED_AT_COLUMN = "CREATED_AT";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private static final String ROLE_NAME_COLUMN = "ROLE_NAME";

    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        User user = null;
        while (rs.next()) {
            if (user == null) {
                long id = rs.getLong(ID_COLUMN);
                String login = rs.getString(LOGIN_COLUMN);
                String password = rs.getString(PASSWORD_COLUMN);
                boolean enabled = rs.getInt(ENABLED_COLUMN) == 1;
                String name = rs.getString(USER_NAME_COLUMN);
                LocalDateTime createdAt = rs.getTimestamp(CREATED_AT_COLUMN).toLocalDateTime();

                user = new User(id, login, password, enabled, name, createdAt, new HashSet<>());
            }

            long roleId = rs.getLong(ROLE_ID_COLUMN);
            String roleName = rs.getString(ROLE_NAME_COLUMN);
            Role role = new Role(roleId, roleName);
            user.addRole(role);
        }

        return user;
    }
}
