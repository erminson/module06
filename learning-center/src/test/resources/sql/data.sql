INSERT INTO STUDENT (NAME, CREATED_AT)
VALUES ('Ivan1', '2010-01-01 11:00:00+03'),
       ('Peter1', '2010-01-01 11:00:00+03'),
       ('John1', '2010-01-01 11:00:00+03');

INSERT INTO TOPIC (TITLE, DURATION_IN_HOURS)
VALUES ('topic11', 1),
       ('topic12', 4),
       ('topic13', 8),
       ('topic14', 8),
       ('topic15', 16),
       ('topic16', 9),
       ('topic21', 2),
       ('topic22', 3),
       ('topic23', 5),
       ('topic24', 2);

INSERT INTO COURSE (title)
VALUES ('Course1'),
       ('Course2');

INSERT INTO COURSE_TOPIC (COURSE_ID, TOPIC_ID, PRIORITY)
VALUES (SELECT id FROM COURSE WHERE TITLE = 'Course1', SELECT id FROM TOPIC WHERE TITLE = 'topic11', 1),
       (SELECT id FROM COURSE WHERE TITLE = 'Course1', SELECT id FROM TOPIC WHERE TITLE = 'topic12', 2),
       (SELECT id FROM COURSE WHERE TITLE = 'Course1', SELECT id FROM TOPIC WHERE TITLE = 'topic13', 3),
       (SELECT id FROM COURSE WHERE TITLE = 'Course1', SELECT id FROM TOPIC WHERE TITLE = 'topic14', 4),
       (SELECT id FROM COURSE WHERE TITLE = 'Course1', SELECT id FROM TOPIC WHERE TITLE = 'topic15', 5),
       (SELECT id FROM COURSE WHERE TITLE = 'Course1', SELECT id FROM TOPIC WHERE TITLE = 'topic16', 6),
       (SELECT id FROM COURSE WHERE TITLE = 'Course2', SELECT id FROM TOPIC WHERE TITLE = 'topic21', 1),
       (SELECT id FROM COURSE WHERE TITLE = 'Course2', SELECT id FROM TOPIC WHERE TITLE = 'topic22', 2),
       (SELECT id FROM COURSE WHERE TITLE = 'Course2', SELECT id FROM TOPIC WHERE TITLE = 'topic23', 3),
       (SELECT id FROM COURSE WHERE TITLE = 'Course2', SELECT id FROM TOPIC WHERE TITLE = 'topic24', 4);

INSERT INTO TOPIC_SCORE (TOPIC_ID, SCORE)
VALUES (SELECT id FROM topic WHERE title = 'topic11', 100),
       (SELECT id FROM topic WHERE title = 'topic12', 100),
       (SELECT id FROM topic WHERE title = 'topic13', 100),
       (SELECT id FROM topic WHERE title = 'topic14', 90),
       (SELECT id FROM topic WHERE title = 'topic15', 0),
       (SELECT id FROM topic WHERE title = 'topic16', 0),
       (SELECT id FROM topic WHERE title = 'topic11', 10),
       (SELECT id FROM topic WHERE title = 'topic12', 0),
       (SELECT id FROM topic WHERE title = 'topic13', 0),
       (SELECT id FROM topic WHERE title = 'topic14', 0),
       (SELECT id FROM topic WHERE title = 'topic15', 0),
       (SELECT id FROM topic WHERE title = 'topic16', 0),
       (SELECT id FROM topic WHERE title = 'topic21', 0),
       (SELECT id FROM topic WHERE title = 'topic22', 90),
       (SELECT id FROM topic WHERE title = 'topic23', 60),
       (SELECT id FROM topic WHERE title = 'topic24', 80);

INSERT INTO RECORD_BOOK (STUDENT_ID, COURSE_ID, START_DATE)
VALUES (SELECT ID FROM STUDENT WHERE NAME = 'Ivan', SELECT ID FROM COURSE WHERE TITLE = 'Course1', '2021-09-15'),
       (SELECT ID FROM STUDENT WHERE NAME = 'Peter', SELECT ID FROM COURSE WHERE TITLE = 'Course1', '2021-09-19'),
       (SELECT ID FROM STUDENT WHERE NAME = 'John', SELECT ID FROM COURSE WHERE TITLE = 'Course2', '2021-09-16');

INSERT INTO RECORD_BOOK_TOPIC_SCORE (RECORD_BOOK_ID, TOPIC_SCORE_ID)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
       (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12),
       (3, 13), (3, 14), (3, 15), (3, 16);