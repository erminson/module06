SELECT C.ID                AS COURSE_ID,
       C.TITLE             AS COURSE_TITLE,
       T.ID                AS TOPIC_ID,
       T.TITLE             AS TOPIC_TITLE,
       T.DURATION_IN_HOURS AS DURATION_IN_HOURS
FROM COURSE_TOPIC CT
         JOIN COURSE C ON CT.COURSE_ID = C.ID
         JOIN TOPIC T ON CT.TOPIC_ID = T.ID
WHERE C.TITLE = ?;