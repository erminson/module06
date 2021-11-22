package ru.erminson.lc.repository.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;
import ru.erminson.lc.repository.RecordBookRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Repository("recordBookRepositoryJdbc")
public class RecordBookRepositoryJdbc implements RecordBookRepository {
    private static final String SQL = "SELECT RB.STUDENT_ID,\n" +
            "       S.NAME,\n" +
            "       RB.ID,\n" +
            "       C.TITLE COURSE_TITLE,\n" +
            "       RB.START_DATE,\n" +
            "       T.TITLE TOPIC_TITLE,\n" +
            "       TS.SCORE,\n" +
            "       T.DURATION_IN_HOURS,\n" +
            "       CT.PRIORITY\n" +
            "FROM RECORD_BOOK RB\n" +
            "         JOIN STUDENT S ON RB.STUDENT_ID = S.ID\n" +
            "         JOIN RECORD_BOOK_TOPIC_SCORE RBTS ON RBTS.RECORD_BOOK_ID = RB.ID\n" +
            "         JOIN TOPIC_SCORE TS ON TS.ID = RBTS.TOPIC_SCORE_ID\n" +
            "         JOIN TOPIC T ON T.ID = TS.TOPIC_ID\n" +
            "         JOIN COURSE_TOPIC CT ON CT.TOPIC_ID = T.ID\n" +
            "         JOIN COURSE C on C.ID = CT.COURSE_ID;";

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

    private final JdbcTemplate jdbcTemplate;
    private Map<Student, RecordBook> storage;

    public RecordBookRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        init();
    }

    private void init() {
        storage = jdbcTemplate.query(SQL, new StudentWithRecordBookExtractor());
    }

    @Override
    @Transactional
    public boolean addStudentWithRecordBook(Student student, RecordBook recordBook) {
        Long studentId = jdbcTemplate.queryForObject(GET_STUDENT_ID_SQL, Long.class, student.getName());
        Long courseId = jdbcTemplate.queryForObject(GET_COURSE_ID_SQL, Long.class, recordBook.getCourseTitle());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_RECORD_BOOK_SQL, new String[]{"ID"});
            ps.setLong(1, studentId);
            ps.setLong(2, courseId);
            ps.setDate(3, Date.valueOf(recordBook.getStartDate()));

            return ps;
        }, keyHolder);

        Long recordBookId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        List<Long> topicScoreIds = insertTopicScores(recordBook.getTopics());
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

        init();
        return true;
    }

    private List<Long> insertTopicScores(List<TopicScore> topicScores) {
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
    public RecordBook getRecordBook(Student student) {
        return storage.get(student);
    }

    @Override
    public boolean isStudentOnCourse(Student student) {
        return storage.containsKey(student);
    }

    @Override
    public boolean removeStudentFromCourse(Student student) {
        storage.remove(student);
        return true;
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(storage.keySet());
    }

    private static class StudentWithRecordBookExtractor implements ResultSetExtractor<Map<Student, RecordBook>> {
        @Override
        public Map<Student, RecordBook> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Student, RecordBook> map = new HashMap<>();

            while (rs.next()) {
                String name = rs.getString(2); // STUDENT_NAME_COLUMN
                Student student = new Student(name);

                if (!map.containsKey(student)) {
                    String courseTitle = rs.getString(4); // COURSE_TITLE_COLUMN
                    LocalDate startDate = rs.getDate(5).toLocalDate();
                    RecordBook recordBook = new RecordBook(courseTitle, startDate, new ArrayList<>());
                    map.put(student, recordBook);
                }

                RecordBook recordBook = map.get(student);

                String topicTitle = rs.getString(6); // TOPIC_TITLE_COLUMN
                int score = rs.getInt(7); // SCORE_COLUMN
                // TODO: convert into duration in days
                Duration durationInHours = Duration.ofHours(rs.getInt(8)); // DURATION_IN_HOURS_COLUMN
                TopicScore topicScore = new TopicScore(topicTitle, score, durationInHours);

                recordBook.addTopic(topicScore);
            }

            return map;
        }
    }
}
