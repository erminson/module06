package ru.erminson.lc.repository.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.repository.AbstractCourseRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("courseRepositoryJdbc")
public class CourseRepositoryJdbc extends AbstractCourseRepository {
    private static final String COURSE_ID_COLUMN = "courseId";
    private static final String COURSE_TITLE_COLUMN = "courseTitle";
    private static final String TOPIC_ID_COLUMN = "topicId";
    private static final String TOPIC_TITLE_COLUMN = "topicTitle";
    private static final String DURATION_IN_HOURS_COLUMN = "durationInHours";

    private static final String SQL =
            String.format(
                    "SELECT \n" +
                            "C.ID %s,\n" +
                            "C.TITLE %s,\n" +
                            "T.ID %s,\n" +
                            "T.TITLE %s,\n" +
                            "T.DURATION_IN_HOURS %s\n" +
                            "FROM COURSE_TOPIC\n" +
                            "JOIN COURSE C ON COURSE_TOPIC.COURSE_ID = C.ID\n" +
                            "JOIN TOPIC T ON COURSE_TOPIC.TOPIC_ID = T.ID;",
                    COURSE_ID_COLUMN, COURSE_TITLE_COLUMN,
                    TOPIC_ID_COLUMN, TOPIC_TITLE_COLUMN, DURATION_IN_HOURS_COLUMN);

    private final JdbcTemplate jdbcTemplate;

    public CourseRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        init();
    }

    private void init() {
        courses = jdbcTemplate.query(SQL, new CourseWithTopicsExtractor());
    }

    private static final class CourseWithTopicsExtractor implements ResultSetExtractor<List<Course>> {
        @Override
        public List<Course> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Course> map = new HashMap<>();
            Course course;
            while (rs.next()) {
                long courseId = rs.getLong(COURSE_ID_COLUMN);
                course = map.computeIfAbsent(courseId, k -> null);
                if (course == null) {
                    course = new Course();
                    course.setTitle(rs.getString(COURSE_TITLE_COLUMN));
                    course.setTopics(new ArrayList<>());
                    map.put(courseId, course);
                }
                long topicId = rs.getLong(TOPIC_ID_COLUMN);
                if (topicId > 0) {
                    Topic topic = new Topic();
                    topic.setTitle(rs.getString(TOPIC_TITLE_COLUMN));
                    topic.setDurationInHours(rs.getInt(DURATION_IN_HOURS_COLUMN));
                    course.addTopic(topic);
                }
            }

            return new ArrayList<>(map.values());
        }
    }
}
