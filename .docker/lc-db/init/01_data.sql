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

INSERT INTO topic (title, duration_in_hours)
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

INSERT INTO course (title, price)
VALUES ('Course1', 300),
       ('Course2', 200);

INSERT INTO orders (student_id, course_id, price, created_at, payment_at, payment_id)
VALUES (3, 1, 300, '2021-09-15 15:20:32', '2021-09-15 16:20:32', 1),
       (4, 1, 300, '2021-09-19 14:10:22', '2021-09-19 15:10:22', 2),
       (5, 2, 200, '2021-09-16 10:20:18', '2021-09-16 11:20:18', 3);

INSERT INTO course_topic (course_id, topic_id, priority)
VALUES ((SELECT id FROM COURSE WHERE title = 'Course1'), (SELECT id FROM TOPIC WHERE title = 'topic11'), 1),
       ((SELECT id FROM COURSE WHERE title = 'Course1'), (SELECT id FROM TOPIC WHERE title = 'topic12'), 2),
       ((SELECT id FROM COURSE WHERE title = 'Course1'), (SELECT id FROM TOPIC WHERE title = 'topic13'), 3),
       ((SELECT id FROM COURSE WHERE title = 'Course1'), (SELECT id FROM TOPIC WHERE title = 'topic14'), 4),
       ((SELECT id FROM COURSE WHERE title = 'Course1'), (SELECT id FROM TOPIC WHERE title = 'topic15'), 5),
       ((SELECT id FROM COURSE WHERE title = 'Course1'), (SELECT id FROM TOPIC WHERE title = 'topic16'), 6),
       ((SELECT id FROM COURSE WHERE title = 'Course2'), (SELECT id FROM TOPIC WHERE title = 'topic21'), 1),
       ((SELECT id FROM COURSE WHERE title = 'Course2'), (SELECT id FROM TOPIC WHERE title = 'topic22'), 2),
       ((SELECT id FROM COURSE WHERE title = 'Course2'), (SELECT id FROM TOPIC WHERE title = 'topic23'), 3),
       ((SELECT id FROM COURSE WHERE title = 'Course2'), (SELECT id FROM TOPIC WHERE title = 'topic24'), 4);

INSERT INTO TOPIC_SCORE (topic_id, score)
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

INSERT INTO record_book (student_id, course_id, start_date)
VALUES ((SELECT id FROM users WHERE name = 'Student1'), (SELECT id FROM course WHERE title = 'Course1'), '2021-09-15'),
       ((SELECT id FROM users WHERE name = 'Student2'), (SELECT id FROM course WHERE title = 'Course1'), '2021-09-19'),
       ((SELECT id FROM users WHERE name = 'Student3'), (SELECT id FROM course WHERE title = 'Course2'), '2021-09-16');

INSERT INTO record_book_topic_score (record_book_id, topic_score_id)
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