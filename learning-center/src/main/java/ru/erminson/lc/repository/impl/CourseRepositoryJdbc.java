package ru.erminson.lc.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.utils.SqlFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class CourseRepositoryJdbc implements CourseRepository {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();
    private SqlFactory sqlFactory;

    public CourseRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setSqlFactory(SqlFactory sqlFactory) {
        this.sqlFactory = sqlFactory;
    }

    @Override
    @Transactional
    public boolean add(Course course) {
        Long courseId = insertCourse(course);
        if (Objects.isNull(courseId)) {
            return false;
        }

        List<Long> topicIds = insertTopicsIntoDB(course.getTopics());
        jdbcTemplate.batchUpdate(
                sqlFactory.getSqlQuery("course/insert-course-topic.sql"),
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Long topicId = topicIds.get(i);
                        ps.setLong(1, courseId);
                        ps.setLong(2, topicId);
                        ps.setInt(3, i);
                    }

                    @Override
                    public int getBatchSize() {
                        return topicIds.size();
                    }
                });

        return true;
    }

    private Long insertCourse(Course course) {
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        sqlFactory.getSqlQuery("course/insert-course.sql"),
                        new String[]{"ID"}
                );
                ps.setString(1, course.getTitle());
                ps.setBigDecimal(2, course.getPrice());

                return ps;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            log.error("Course: {} hasn't added. {}", course.getTitle(), e.getMessage());
            return null;
        }

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private List<Long> insertTopicsIntoDB(List<Topic> topics) {
        List<Long> ids = new ArrayList<>();
        topics.forEach(topic -> {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        sqlFactory.getSqlQuery("course/insert-topics.sql"),
                        new String[]{"ID"}
                );
                ps.setString(1, topic.getTitle());
                ps.setInt(2, topic.getDurationInHours());
                return ps;
            }, keyHolder);

            ids.add(Objects.requireNonNull(keyHolder.getKey()).longValue());
        });

        return ids;
    }

    @Override
    public boolean isExistCourseByTitle(String courseTitle) {
        String sql = sqlFactory.getSqlQuery("course/select-course-row-by-title.sql");
        RowCountCallbackHandler handler = new RowCountCallbackHandler();
        jdbcTemplate.query(sql, handler, courseTitle);
        return handler.getRowCount() > 0;
    }

    @Override
    public List<Course> getAllCourses() {
        String sql = sqlFactory.getSqlQuery("course/select-all-courses.sql");
        return jdbcTemplate.query(sql, new CoursesWithTopicsExtractor());
    }

    @Override
    public Optional<Course> findById(long id) {
        String sql = sqlFactory.getSqlQuery("course/select-course-by-id.sql");
        List<Course> courses = jdbcTemplate.query(sql, new CoursesWithTopicsExtractor(), id);

        if (courses == null || courses.isEmpty()) {
            log.error("Course: {} not found", id);
            return Optional.empty();
        }

        return Optional.ofNullable(courses.get(0));
    }

    @Override
    public Course getCourseByTitle(String title) throws IllegalInitialDataException {
        String sql = sqlFactory.getSqlQuery("course/select-course-by-title.sql");
        List<Course> courses = jdbcTemplate.query(sql, new CoursesWithTopicsExtractor(), title);

        if (courses == null || courses.isEmpty()) {
            log.error("Course: {} not found", title);
            throw new IllegalInitialDataException(String.format("Course '%s' not found", title));
        }

        return courses.get(0);
    }

    @Override
    public List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException {
        Course course = this.getCourseByTitle(title);
        return course.getTopics();
    }

    private static final class CoursesWithTopicsExtractor implements ResultSetExtractor<List<Course>> {
        @Override
        public List<Course> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Course> map = new HashMap<>();
            while (rs.next()) {
                long courseId = rs.getLong("COURSE_ID");
                Course course = map.get(courseId);
                if (course == null) {
                    course = new Course();
                    course.setId(courseId);
                    course.setTitle(rs.getString("COURSE_TITLE"));
                    course.setPrice(rs.getBigDecimal("COURSE_PRICE"));
                    course.setTopics(new ArrayList<>());
                    map.put(courseId, course);
                }
                long topicId = rs.getLong("TOPIC_ID");
                if (topicId > 0) {
                    Topic topic = new Topic();
                    topic.setId(topicId);
                    topic.setTitle(rs.getString("TOPIC_TITLE"));
                    topic.setDurationInHours(rs.getInt("DURATION_IN_HOURS"));
                    course.addTopic(topic);
                }
            }

            return new ArrayList<>(map.values());
        }
    }
}