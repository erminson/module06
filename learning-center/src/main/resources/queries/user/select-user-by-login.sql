SELECT U.ID ID,
       LOGIN,
       PASSWORD,
       ENABLED,
       U.NAME USER_NAME,
       CREATED_AT,
       ROLE_ID,
       R.NAME ROLE_NAME
FROM USER U
         JOIN USER_ROLE UR ON U.ID = UR.user_id
         JOIN ROLE R ON UR.ROLE_ID = R.ID
WHERE login = ?