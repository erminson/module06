package ru.erminson.lc.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.entity.TopicScore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RecordBookExtractor implements ResultSetExtractor<RecordBook> {
    @Override
    public RecordBook extractData(ResultSet rs) throws SQLException, DataAccessException {
        RecordBook recordBook = null;
        while (rs.next()) {
            if (recordBook == null) {
                long recordBookId = rs.getLong(StudentRecordBookExtractor.RECORD_BOOK_ID_COLUMN);
                String courseTitle = rs.getString(StudentRecordBookExtractor.COURSE_TITLE_COLUMN);
                LocalDate startDate = rs.getDate(StudentRecordBookExtractor.START_DATE_COLUMN).toLocalDate();
                long courseId = rs.getLong(StudentRecordBookExtractor.COURSE_ID_COLUMN);
                recordBook = new RecordBook(recordBookId, courseId, courseTitle, startDate, new ArrayList<>());
            }

            long topicScoreId = rs.getLong(StudentRecordBookExtractor.TOPIC_SCORE_ID_COLUMN);
            long topicId = rs.getLong(StudentRecordBookExtractor.TOPIC_ID_COLUMN);
            String topicTitle = rs.getString(StudentRecordBookExtractor.TOPIC_TITLE_COLUMN);
            int score = rs.getInt(StudentRecordBookExtractor.TOPIC_SCORE_COLUMN);
            int durationInHours = rs.getInt(StudentRecordBookExtractor.DURATION_IN_HOURS_COLUMN);
            Topic topic = new Topic(topicId, topicTitle, durationInHours);

            TopicScore topicScore = TopicScore.create(topicScoreId, score, topic);

            recordBook.addTopic(topicScore);
        }

        return recordBook;
    }
}
