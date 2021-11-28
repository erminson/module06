package ru.erminson.lc.repository.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.CourseRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CourseRepositoryJdbc implements CourseRepository {
    private static final String COURSE_ID_COLUMN = "COURSE_ID";
    private static final String COURSE_TITLE_COLUMN = "COURSE_TITLE";
    private static final String TOPIC_ID_COLUMN = "TOPIC_ID";
    private static final String TOPIC_TITLE_COLUMN = "TOPIC_TITLE";
    private static final String DURATION_IN_HOURS_COLUMN = "DURATION_IN_HOURS";

    private static final String GET_ALL_COURSES_SQL =
            String.format(
                    "SELECT \n" +
                            "C.ID %s,\n" +
                            "C.TITLE %s,\n" +
                            "T.ID %s,\n" +
                            "T.TITLE %s,\n" +
                            "T.DURATION_IN_HOURS %s\n" +
                            "FROM COURSE_TOPIC CT\n" +
                            "JOIN COURSE C ON CT.COURSE_ID = C.ID\n" +
                            "JOIN TOPIC T ON CT.TOPIC_ID = T.ID",
                    COURSE_ID_COLUMN, COURSE_TITLE_COLUMN,
                    TOPIC_ID_COLUMN, TOPIC_TITLE_COLUMN, DURATION_IN_HOURS_COLUMN);
    private static final String GET_COURSE_BY_TITLE_SQL =
            String.format("%s\nWHERE C.TITLE = ?", GET_ALL_COURSES_SQL);

    private static final String ADD_COURSE_SQL = "INSERT INTO COURSE (TITLE) VALUES (?)";
    private static final String ADD_TOPIC_SQL = "INSERT INTO TOPIC (TITLE, DURATION_IN_HOURS) VALUES (?, ?)";
    private static final String ADD_COURSE_TOPIC_SQL =
            "INSERT INTO COURSE_TOPIC (COURSE_ID, TOPIC_ID, PRIORITY)\n" +
                    "VALUES (?, ?, ?);";
    private static final String GET_COURSE_ROW_BY_TITLE_SQL =
            String.format(
                    "SELECT ID %s, TITLE %s FROM COURSE WHERE COURSE.TITLE = ?",
                    COURSE_ID_COLUMN, COURSE_TITLE_COLUMN);

    private final JdbcTemplate jdbcTemplate;

    public CourseRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public boolean add(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(ADD_COURSE_SQL, new String[]{"ID"});
            ps.setString(1, course.getTitle());

            return ps;
        }, keyHolder);

        Long courseId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        List<Long> topicIds = insertTopicsIntoDB(course.getTopics());
        jdbcTemplate.batchUpdate(ADD_COURSE_TOPIC_SQL, new BatchPreparedStatementSetter() {
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

    private List<Long> insertTopicsIntoDB(List<Topic> topics) {
        List<Long> ids = new ArrayList<>();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        topics.forEach(topic -> {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(ADD_TOPIC_SQL, new String[]{"ID"});
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
        Course course = jdbcTemplate.queryForObject(
                GET_COURSE_ROW_BY_TITLE_SQL,
                (rs, rowNum) -> new Course(
                        rs.getLong(COURSE_ID_COLUMN),
                        rs.getString(COURSE_TITLE_COLUMN)
                ),
                courseTitle
        );

        return course == null;
    }

    @Override
    public List<Course> getAllCourses() {
        return jdbcTemplate.query(GET_ALL_COURSES_SQL, new CourseWithTopicsExtractor());
    }

    @Override
    public Course getCourseByTitle(String title) throws IllegalInitialDataException {
        List<Course> courses = jdbcTemplate.query(GET_COURSE_BY_TITLE_SQL, new CourseWithTopicsExtractor(), title);
        assert courses != null;
        if (courses.isEmpty()) {
            throw new IllegalInitialDataException(String.format("Course '%s' not found", title));
        }

        return courses.get(0);
    }

    @Override
    public List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException {
        Course course = this.getCourseByTitle(title);
        return course.getTopics();
    }

    private static final class CourseWithTopicsExtractor implements ResultSetExtractor<List<Course>> {
        @Override
        public List<Course> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Course> map = new HashMap<>();
            while (rs.next()) {
                long courseId = rs.getLong(COURSE_ID_COLUMN);
                Course course = map.get(courseId);
                if (course == null) {
                    course = new Course();
                    course.setId(courseId);
                    course.setTitle(rs.getString(COURSE_TITLE_COLUMN));
                    course.setTopics(new ArrayList<>());
                    map.put(courseId, course);
                }
                long topicId = rs.getLong(TOPIC_ID_COLUMN);
                if (topicId > 0) {
                    Topic topic = new Topic();
                    topic.setId(topicId);
                    topic.setTitle(rs.getString(TOPIC_TITLE_COLUMN));
                    topic.setDurationInHours(rs.getInt(DURATION_IN_HOURS_COLUMN));
                    course.addTopic(topic);
                }
            }

            return new ArrayList<>(map.values());
        }
    }
}
