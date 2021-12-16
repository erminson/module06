INSERT INTO role (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_TEACHER'),
       ('ROLE_STUDENT');

INSERT INTO users (login, password, name)
VALUES ('admin1', '$2a$12$uV66DMJTnlReDUqlIsM5neJu0GLmE9cJzJxuAdHwpfaM6ANOiE7Uu', 'Admin1'),     -- admin1
       ('teacher1', '$2a$12$bdvFkIYNBj.GjxFPwVUikOyCG5Gh0vTy6TCivQnu0GJf5xpJMgL46', 'Teacher1'), -- teacher1
       ('student1', '$2a$10$rqtxo3EGCEd2AEJR2OWDle2ZgANo2qNRNsX8Tu8tr7h3KF1UVYrQi', 'Student1'), -- student1
       ('student2', '$2a$12$HgCz/TdbB1rkr1HvBl5HbOdoBI5M6finENrq4gFvc/H67f6lFOLMK', 'Student2'), -- student2
       ('student3', '$2a$12$S1W/G1SN783e2aRrwqZU9exOhwk6EjafsOel3yTyufDh4E0MVPPZG', 'Student3'), -- student2
       ('student4', '$2a$12$5nSdJwVMubTKBi1kyxl0geoVMmybCjS4pERr81uurcP2SaxMTAPUW', 'Student4'); -- student2

INSERT INTO user_role (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 3),
       (5, 3),
       (5, 2),
       (6, 3);

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

INSERT INTO COURSE (title, price)
VALUES ('Course1', 300),
       ('Course2', 200);

INSERT INTO ORDERS (STUDENT_ID, COURSE_ID, PRICE, CREATED_AT, PAYMENT_AT, PAYMENT_ID)
VALUES (3, 1, 300, '2021-09-15 15:20:32', '2021-09-15 16:20:32', 1),
       (4, 1, 300, '2021-09-19 14:10:22', '2021-09-19 15:10:22', 2),
       (5, 2, 200, '2021-09-16 10:20:18', '2021-09-16 11:20:18', 3);

INSERT INTO COURSE_TOPIC (COURSE_ID, TOPIC_ID, PRIORITY)
VALUES ((SELECT id FROM COURSE WHERE TITLE = 'Course1'), (SELECT id FROM TOPIC WHERE TITLE = 'topic11'), 1),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course1'), (SELECT id FROM TOPIC WHERE TITLE = 'topic12'), 2),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course1'), (SELECT id FROM TOPIC WHERE TITLE = 'topic13'), 3),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course1'), (SELECT id FROM TOPIC WHERE TITLE = 'topic14'), 4),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course1'), (SELECT id FROM TOPIC WHERE TITLE = 'topic15'), 5),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course1'), (SELECT id FROM TOPIC WHERE TITLE = 'topic16'), 6),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course2'), (SELECT id FROM TOPIC WHERE TITLE = 'topic21'), 1),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course2'), (SELECT id FROM TOPIC WHERE TITLE = 'topic22'), 2),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course2'), (SELECT id FROM TOPIC WHERE TITLE = 'topic23'), 3),
       ((SELECT id FROM COURSE WHERE TITLE = 'Course2'), (SELECT id FROM TOPIC WHERE TITLE = 'topic24'), 4);

INSERT INTO TOPIC_SCORE (TOPIC_ID, SCORE)
VALUES ((SELECT id FROM topic WHERE title = 'topic11'), 100),
       ((SELECT id FROM topic WHERE title = 'topic12'), 100),
       ((SELECT id FROM topic WHERE title = 'topic13'), 100),
       ((SELECT id FROM topic WHERE title = 'topic14'), 90),
       ((SELECT id FROM topic WHERE title = 'topic15'), 0),
       ((SELECT id FROM topic WHERE title = 'topic16'), 0),
       ((SELECT id FROM topic WHERE title = 'topic11'), 10),
       ((SELECT id FROM topic WHERE title = 'topic12'), 0),
       ((SELECT id FROM topic WHERE title = 'topic13'), 0),
       ((SELECT id FROM topic WHERE title = 'topic14'), 0),
       ((SELECT id FROM topic WHERE title = 'topic15'), 0),
       ((SELECT id FROM topic WHERE title = 'topic16'), 0),
       ((SELECT id FROM topic WHERE title = 'topic21'), 0),
       ((SELECT id FROM topic WHERE title = 'topic22'), 90),
       ((SELECT id FROM topic WHERE title = 'topic23'), 60),
       ((SELECT id FROM topic WHERE title = 'topic24'), 80);

INSERT INTO RECORD_BOOK (STUDENT_ID, COURSE_ID, START_DATE)
VALUES ((SELECT ID FROM USERS WHERE NAME = 'Student1'), (SELECT ID FROM COURSE WHERE TITLE = 'Course1'), '2021-09-15'),
       ((SELECT ID FROM USERS WHERE NAME = 'Student2'), (SELECT ID FROM COURSE WHERE TITLE = 'Course1'), '2021-09-19'),
       ((SELECT ID FROM USERS WHERE NAME = 'Student3'), (SELECT ID FROM COURSE WHERE TITLE = 'Course2'), '2021-09-16');

INSERT INTO RECORD_BOOK_TOPIC_SCORE (RECORD_BOOK_ID, TOPIC_SCORE_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (2, 7),
       (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12),
       (3, 13),
       (3, 14),
       (3, 15),
       (3, 16);