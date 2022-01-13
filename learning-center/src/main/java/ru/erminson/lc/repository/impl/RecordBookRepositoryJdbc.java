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
import ru.erminson.lc.mapper.RecordBookExtractor;
import ru.erminson.lc.mapper.StudentRecordBookExtractor;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.utils.SqlFactory;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class RecordBookRepositoryJdbc implements RecordBookRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SqlFactory sqlFactory;

    public RecordBookRepositoryJdbc(JdbcTemplate jdbcTemplate, SqlFactory sqlFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlFactory = sqlFactory;
    }

    @Override
    @Transactional
    public boolean addStudentWithRecordBook(Student student, RecordBook recordBook) {
        return enrollStudentOnCourse(student.getId(), recordBook);
    }

    @Override
    @Transactional
    public boolean enrollStudentOnCourse(long studentId, RecordBook recordBook) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            String sql = sqlFactory.getSqlQuery("recordbook/insert-recordbook.sql");
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                ps.setLong(1, studentId);
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
        String sql = sqlFactory.getSqlQuery("recordbook-topicscore/insert-rb-ts.sql");
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
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
        String sql = sqlFactory.getSqlQuery("topicscore/insert-topicscore.sql");
        for (TopicScore topicScore : topicScores) { // TODO: fix it
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
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
        return rateTopic(topicScore.getId(), score);
    }

    @Override
    public boolean rateTopic(long topicScoreId, int score) {
        int updatedCount = jdbcTemplate.update(
                sqlFactory.getSqlQuery("topicscore/update-topicscore.sql"),
                score,
                topicScoreId
        );
        return updatedCount > 0;
    }

    @Override
    public RecordBook getRecordBook(String studentName) {
        Map<Student, RecordBook> map = jdbcTemplate.query(
                sqlFactory.getSqlQuery("recordbook/select-recordbook-by-student-name.sql"),
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
    public Optional<RecordBook> findByStudentId(long studentId) {
        String sql = sqlFactory.getSqlQuery("recordbook/select-recordbook-by-student-id.sql");
        RecordBook recordBook = null;

        try {
            recordBook = jdbcTemplate.query(sql, new RecordBookExtractor(), studentId);
            log.debug("Record book for student with id: {} was found", studentId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Record book for student wit id: {} wasn't found", studentId);
        }

        return Optional.ofNullable(recordBook);
    }

    @Override
    public boolean isStudentOnCourse(Student student) {
        List<Long> ids = jdbcTemplate.query(
                sqlFactory.getSqlQuery("recordbook/select-recordbook-row-by-student-id.sql"),
                (rs, rowNum) -> rs.getLong(1),
                student.getId());

        return !ids.isEmpty();
    }

    @Override
    @Transactional
    public boolean removeStudentFromCourse(Student student) {
        Long recordBookId;
        try {
            recordBookId = jdbcTemplate.queryForObject(
                    sqlFactory.getSqlQuery("recordbook/select-recordbook-id.sql"),
                    Long.class,
                    student.getId()
            );
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            return false;
        }

        int deletedTopicsNumber = jdbcTemplate.update(
                sqlFactory.getSqlQuery("topicscore/delete-topicscore.sql"),
                recordBookId
        );
        int deletedRecordBooksNumber = jdbcTemplate.update(
                sqlFactory.getSqlQuery("recordbook/delete-recordbook-by-id.sql"),
                recordBookId
        );

        return deletedTopicsNumber > 0 || deletedRecordBooksNumber > 0;
    }

    @Override
    public List<Student> getAllStudents() {
        String sql = sqlFactory.getSqlQuery("recordbook/select-all-student-on-courses.sql");
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student student = new Student();
            student.setId(rs.getLong(StudentRecordBookExtractor.STUDENT_ID_COLUMN));
            student.setName(rs.getString(StudentRecordBookExtractor.STUDENT_NAME_COLUMN));

            return student;
        });
    }
}
