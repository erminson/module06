package ru.erminson.lc.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.entity.TopicScore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentRecordBookExtractor implements ResultSetExtractor<Map<Student, RecordBook>> {
    public static final String RECORD_BOOK_ID_COLUMN = "RECORD_BOOK_ID";
    public static final String STUDENT_ID_COLUMN = "STUDENT_ID";
    public static final String STUDENT_NAME_COLUMN = "STUDENT_NAME";
    public static final String COURSE_ID_COLUMN = "COURSE_ID";
    public static final String COURSE_TITLE_COLUMN = "COURSE_TITLE";
    public static final String START_DATE_COLUMN = "START_DATE";
    public static final String TOPIC_SCORE_ID_COLUMN = "TOPIC_SCORE_ID";
    public static final String TOPIC_ID_COLUMN = "TOPIC_ID";
    public static final String TOPIC_TITLE_COLUMN = "TOPIC_TITLE";
    public static final String TOPIC_SCORE_COLUMN = "TOPIC_SCORE";
    public static final String DURATION_IN_HOURS_COLUMN = "DURATION_IN_HOURS";
    public static final String PRIORITY_COLUMN = "PRIORITY_HOURS";
    
    @Override
    public Map<Student, RecordBook> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Student, RecordBook> map = new HashMap<>();

        while (rs.next()) {
            long studentId = rs.getLong(STUDENT_ID_COLUMN);
            String name = rs.getString(STUDENT_NAME_COLUMN);
            Student student = new Student(studentId, name);

            if (!map.containsKey(student)) {
                long recordBookId = rs.getLong(RECORD_BOOK_ID_COLUMN);
                String courseTitle = rs.getString(COURSE_TITLE_COLUMN);
                LocalDate startDate = rs.getDate(START_DATE_COLUMN).toLocalDate();
                long courseId = rs.getLong(COURSE_ID_COLUMN);
                RecordBook recordBook = new RecordBook(recordBookId, courseId, courseTitle, startDate, new ArrayList<>());
                map.put(student, recordBook);
            }

            RecordBook recordBook = map.get(student);

            long topicScoreId = rs.getLong(TOPIC_SCORE_ID_COLUMN);
            long topicId = rs.getLong(TOPIC_ID_COLUMN);
            String topicTitle = rs.getString(TOPIC_TITLE_COLUMN);
            int score = rs.getInt(TOPIC_SCORE_COLUMN);
            int durationInHours = rs.getInt(DURATION_IN_HOURS_COLUMN);
            Topic topic = new Topic(topicId, topicTitle, durationInHours);

            TopicScore topicScore = TopicScore.create(topicScoreId, score, topic);

            recordBook.addTopic(topicScore);
        }

        return map;
    }
}
