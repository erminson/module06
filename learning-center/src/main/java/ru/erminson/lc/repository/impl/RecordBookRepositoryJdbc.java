package ru.erminson.lc.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.erminson.lc.mapper.StudentRecordBookExtractor;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;
import ru.erminson.lc.repository.RecordBookRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class RecordBookRepositoryJdbc implements RecordBookRepository {
    private static final String GET_ALL_RECORD_BOOKS_SQL = String.format(
            "SELECT\n" +
                    "       RB.ID               %s,\n" +
                    "       RB.STUDENT_ID       %s,\n" +
                    "       S.NAME              %s,\n" +
                    "       C.ID                %s,\n" +
                    "       C.TITLE             %s,\n" +
                    "       RB.START_DATE       %s,\n" +
                    "       TS.ID               %s,\n" +
                    "       T.ID                %s,\n" +
                    "       T.TITLE             %s,\n" +
                    "       TS.SCORE            %s,\n" +
                    "       T.DURATION_IN_HOURS %s,\n" +
                    "       CT.PRIORITY         %s\n" +
                    "FROM RECORD_BOOK RB\n" +
                    "         JOIN STUDENT S ON RB.STUDENT_ID = S.ID\n" +
                    "         JOIN RECORD_BOOK_TOPIC_SCORE RBTS ON RBTS.RECORD_BOOK_ID = RB.ID\n" +
                    "         JOIN TOPIC_SCORE TS ON TS.ID = RBTS.TOPIC_SCORE_ID\n" +
                    "         JOIN TOPIC T ON T.ID = TS.TOPIC_ID\n" +
                    "         JOIN COURSE_TOPIC CT ON CT.TOPIC_ID = T.ID\n" +
                    "         JOIN COURSE C on C.ID = CT.COURSE_ID",
            StudentRecordBookExtractor.RECORD_BOOK_ID_COLUMN, StudentRecordBookExtractor.STUDENT_ID_COLUMN,
            StudentRecordBookExtractor.STUDENT_NAME_COLUMN, StudentRecordBookExtractor.COURSE_ID_COLUMN,
            StudentRecordBookExtractor.COURSE_TITLE_COLUMN, StudentRecordBookExtractor.START_DATE_COLUMN,
            StudentRecordBookExtractor.TOPIC_SCORE_ID_COLUMN, StudentRecordBookExtractor.TOPIC_ID_COLUMN,
            StudentRecordBookExtractor.TOPIC_TITLE_COLUMN, StudentRecordBookExtractor.TOPIC_SCORE_COLUMN,
            StudentRecordBookExtractor.DURATION_IN_HOURS_COLUMN, StudentRecordBookExtractor.PRIORITY_COLUMN
    );
    private static final String GET_RECORD_BOOK_BY_STUDENT_NAME_SQL =
            String.format("%s\nWHERE S.NAME = ?", GET_ALL_RECORD_BOOKS_SQL);

    private static final String INSERT_RECORD_BOOK_SQL =
            "INSERT INTO RECORD_BOOK (STUDENT_ID, COURSE_ID, START_DATE)\n" +
                    "VALUES (?, ?, ?);";

    private static final String INSERT_TOPIC_SCORE_SQL =
            "INSERT INTO TOPIC_SCORE (TOPIC_ID, SCORE)\n" +
                    "VALUES (?, ?);";

    private static final String INSERT_RECORD_BOOK_TOPIC_SCORE_SQL =
            "INSERT INTO RECORD_BOOK_TOPIC_SCORE (RECORD_BOOK_ID, TOPIC_SCORE_ID)\n" +
                    "VALUES (?, ?)";

    private static final String GET_STUDENT_ID_SQL = "SELECT ID FROM STUDENT WHERE NAME = ?";
    private static final String GET_COURSE_ID_SQL = "SELECT ID FROM COURSE WHERE TITLE = ?";

    private static final String GET_ALL_STUDENTS_ON_COURSES =
            String.format(
                    "SELECT\n" +
                            "S.ID   %s,\n" +
                            "S.NAME %s\n" +
                            "FROM RECORD_BOOK RB\n" +
                            "JOIN STUDENT S ON RB.STUDENT_ID = S.ID",
                    StudentRecordBookExtractor.STUDENT_ID_COLUMN, StudentRecordBookExtractor.STUDENT_NAME_COLUMN
            );

    private static final String GET_RECORD_BOOK_ID_BY_STUDENT_ID =
            "SELECT ID FROM RECORD_BOOK WHERE STUDENT_ID = ?";

    private static final String UPDATE_TOPIC_SCORE_SQL =
            "UPDATE TOPIC_SCORE\n" +
                    "SET score = ?\n" +
                    "WHERE id = ?;";

    private static final String GET_RECORD_BOOK_ID =
            "SELECT ID\n" +
                    "FROM RECORD_BOOK\n" +
                    "WHERE STUDENT_ID = ?;";

    private static final String DELETE_TOPIC_SCORES_SQL =
            "DELETE\n" +
                    "FROM TOPIC_SCORE\n" +
                    "WHERE ID IN (SELECT TS.ID\n" +
                    "    FROM TOPIC_SCORE TS\n" +
                    "    JOIN RECORD_BOOK_TOPIC_SCORE RBTS ON RBTS.TOPIC_SCORE_ID = TS.ID\n" +
                    "    WHERE RBTS.RECORD_BOOK_ID = ?);";

    private static final String DELETE_RECORD_BOOK_BY_ID_SQL =
            "DELETE\n" +
                    "FROM RECORD_BOOK\n" +
                    "WHERE ID = ?;";

    private final JdbcTemplate jdbcTemplate;

    public RecordBookRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public boolean addStudentWithRecordBook(Student student, RecordBook recordBook) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(INSERT_RECORD_BOOK_SQL, new String[]{"ID"});
                ps.setLong(1, student.getId());
                ps.setLong(2, recordBook.getCourseId());
                ps.setDate(3, Date.valueOf(recordBook.getStartDate()));
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            log.error("Record book: {} wasn't added", recordBook.toString());
            return false;
        }

        Long recordBookId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        List<Long> topicScoreIds = insertTopicScoresIntoDB(recordBook.getTopics());
        jdbcTemplate.batchUpdate(INSERT_RECORD_BOOK_TOPIC_SCORE_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long topicScoreId = topicScoreIds.get(i);
                ps.setLong(1, recordBookId);
                ps.setLong(2, topicScoreId);
            }

            @Override
            public int getBatchSize() {
                return topicScoreIds.size();
            }
        });

        return true;
    }

    private List<Long> insertTopicScoresIntoDB(List<TopicScore> topicScores) {
        List<Long> ids = new ArrayList<>();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        for (TopicScore topicScore : topicScores) {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(INSERT_TOPIC_SCORE_SQL, new String[]{"ID"});
                ps.setLong(1, topicScore.getTopicId());
                ps.setInt(2, topicScore.getScore());

                return ps;
            }, keyHolder);

            ids.add(Objects.requireNonNull(keyHolder.getKey()).longValue());
        }

        return ids;
    }

    @Override
    public boolean rateTopic(TopicScore topicScore, int score) {
        int updatedCount = jdbcTemplate.update(UPDATE_TOPIC_SCORE_SQL, score, topicScore.getId());
        return updatedCount > 0;
    }

    @Override
    public RecordBook getRecordBook(String studentName) {
        Map<Student, RecordBook> map = jdbcTemplate.query(
                GET_RECORD_BOOK_BY_STUDENT_NAME_SQL,
                new StudentRecordBookExtractor(),
                studentName
        );

        if (Objects.isNull(map)) {
            //TODO: throws exception
            return null;
        }

        Student student = map.keySet().stream()
                .filter(s -> s.getName().equals(studentName))
                .findFirst()
                .orElseGet(() -> new Student(studentName));

        return map.get(student);
    }

    @Override
    public RecordBook getRecordBook(Student student) {
        return getRecordBook(student.getName());
    }

    @Override
    public boolean isStudentOnCourse(Student student) {
        List<Long> ids = jdbcTemplate.query(
                GET_RECORD_BOOK_ID_BY_STUDENT_ID,
                (rs, rowNum) -> rs.getLong(1),
                student.getId());

        return !ids.isEmpty();
    }

    @Override
    @Transactional
    public boolean removeStudentFromCourse(Student student) {
        Long recordBookId;
        try {
            recordBookId = jdbcTemplate.queryForObject(GET_RECORD_BOOK_ID, Long.class, student.getId());
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return false;
        }

        int deletedTopicsNumber = jdbcTemplate.update(DELETE_TOPIC_SCORES_SQL, recordBookId);
        int deletedRecordBooksNumber = jdbcTemplate.update(DELETE_RECORD_BOOK_BY_ID_SQL, recordBookId);

        return deletedTopicsNumber > 0 || deletedRecordBooksNumber > 0;
    }

    @Override
    public List<Student> getAllStudents() {
        return jdbcTemplate.query(GET_ALL_STUDENTS_ON_COURSES, (rs, rowNum) -> {
            Student student = new Student();
            student.setId(rs.getLong(StudentRecordBookExtractor.STUDENT_ID_COLUMN));
            student.setName(rs.getString(StudentRecordBookExtractor.STUDENT_NAME_COLUMN));

            return student;
        });
    }
}
