CREATE TABLE IF NOT EXISTS role
(
    id   IDENTITY     NOT NULL PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user
(
    id         IDENTITY     NOT NULL PRIMARY KEY,
    login      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(128) NOT NULL,
    enabled    TINYINT               DEFAULT 1,
    name       VARCHAR(128),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_role
(
    id      IDENTITY NOT NULL PRIMARY KEY,
    user_id BIGINT REFERENCES user (id),
    role_id BIGINT REFERENCES role (id),
    UNIQUE (user_id, role_id)
);

-- CREATE TABLE IF NOT EXISTS student
-- (
--     id      IDENTITY     NOT NULL PRIMARY KEY,
--     name    VARCHAR(128) NOT NULL UNIQUE,
--     user_id BIGINT REFERENCES user (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS teacher
-- (
--     id      IDENTITY     NOT NULL PRIMARY KEY,
--     name    VARCHAR(128) NOT NULL UNIQUE,
--     user_id BIGINT REFERENCES user (id) ON DELETE CASCADE
-- );

CREATE TABLE IF NOT EXISTS topic
(
    id                IDENTITY     NOT NULL PRIMARY KEY,
    title             VARCHAR(128) NOT NULL UNIQUE,
    duration_in_hours SMALLINT     NOT NULL
);

CREATE TABLE IF NOT EXISTS course
(
    id    IDENTITY     NOT NULL PRIMARY KEY,
    title VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS course_topic
(
    id        IDENTITY NOT NULL PRIMARY KEY,
    course_id BIGINT REFERENCES course (id),
    topic_id  BIGINT REFERENCES topic (id),
    priority  SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS topic_score
(
    id       IDENTITY NOT NULL PRIMARY KEY,
    topic_id BIGINT REFERENCES topic (id),
    score    SMALLINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS record_book
(
    id         IDENTITY NOT NULL PRIMARY KEY,
    student_id BIGINT REFERENCES user (id) ON DELETE CASCADE,
    course_id  BIGINT   REFERENCES course (id) ON DELETE SET NULL,
    start_date DATE     NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS record_book_topic_score
(
    id             IDENTITY NOT NULL PRIMARY KEY,
    record_book_id BIGINT REFERENCES record_book (id) ON DELETE CASCADE,
    topic_score_id BIGINT   REFERENCES topic_score (id) ON DELETE SET NULL
);