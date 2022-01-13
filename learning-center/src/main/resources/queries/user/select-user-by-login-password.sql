SELECT users.id,
       login,
       password,
       CREATED_AT,
       role.NAME
FROM users
         JOIN role ON users.ROLE_ID = role.ID
WHERE LOGIN = ? AND PASSWORD = ?