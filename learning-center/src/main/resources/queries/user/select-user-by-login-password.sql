SELECT user.id,
       login,
       password,
       CREATED_AT,
       role.NAME
FROM user
         JOIN role ON user.ROLE_ID = role.ID
WHERE LOGIN = ? AND PASSWORD = ?