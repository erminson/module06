CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL     NOT NULL PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL     PRIMARY KEY,
    login      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(128) NOT NULL,
    enabled    SMALLINT               DEFAULT 1,
    name       VARCHAR(128),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_role
(
    id      SERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id),
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
    id                SERIAL     NOT NULL PRIMARY KEY,
    title             VARCHAR(128) NOT NULL UNIQUE,
    duration_in_hours SMALLINT     NOT NULL
);

CREATE TABLE IF NOT EXISTS course
(
    id    SERIAL     NOT NULL PRIMARY KEY,
    title VARCHAR(128) NOT NULL UNIQUE,
    price NUMERIC DEFAULT 0
);

CREATE TABLE IF NOT EXISTS orders
(
    id         SERIAL NOT NULL PRIMARY KEY,
    student_id BIGINT REFERENCES users (id),
    course_id  BIGINT REFERENCES course (id),
    price      NUMERIC,
    created_at TIMESTAMP,
    payment_at TIMESTAMP,
    payment_id BIGINT UNIQUE
);

CREATE TABLE IF NOT EXISTS course_topic
(
    id        SERIAL NOT NULL PRIMARY KEY,
    course_id BIGINT REFERENCES course (id),
    topic_id  BIGINT REFERENCES topic (id),
    priority  SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS topic_score
(
    id       SERIAL NOT NULL PRIMARY KEY,
    topic_id BIGINT REFERENCES topic (id),
    score    SMALLINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS record_book
(
    id         SERIAL NOT NULL PRIMARY KEY,
    student_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    course_id  BIGINT   REFERENCES course (id) ON DELETE SET NULL,
    start_date DATE     NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS record_book_topic_score
(
    id             SERIAL NOT NULL PRIMARY KEY,
    record_book_id BIGINT REFERENCES record_book (id) ON DELETE CASCADE,
    topic_score_id BIGINT   REFERENCES topic_score (id) ON DELETE SET NULL
);