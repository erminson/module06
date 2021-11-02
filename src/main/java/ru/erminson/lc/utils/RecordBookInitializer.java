package ru.erminson.lc.utils;

import ru.erminson.lc.model.dto.RecordBookDto;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.entity.TopicScore;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RecordBookInitializer {
    private static final double TEACHING_HOURS_PER_DAY = 8;

    private RecordBookInitializer() {
        throw new IllegalStateException("RecordBookInitializer utility class");
    }

    public static RecordBook createRecordBookByCourseAndStartDate(Course course, LocalDate startDate) {
        String courseTitle = course.getTitle();
        List<TopicScore> topics = course.getTopics().stream()
                .map(RecordBookInitializer::createTopic)
                .collect(Collectors.toList());

        return new RecordBook(courseTitle, startDate, topics);
    }

    public static RecordBook createRecordBookByCourse(Course course) {
        LocalDate startDate = LocalDate.now().plusDays(1);
        return createRecordBookByCourseAndStartDate(course, startDate);
    }

    public static RecordBook createRecordBookByRecordBookDto(RecordBookDto recordBookDto) {
        String courseTitle = recordBookDto.getCourseTitle();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(recordBookDto.getStartDate(), formatter);
        List<TopicScore> topics = recordBookDto.getTopics().stream()
                .map(topicScoreDto -> new TopicScore(
                                topicScoreDto.getTopicTitle(),
                                topicScoreDto.getScore(),
                                Duration.ofDays(topicScoreDto.getDurationInDays())
                        )
                )
                .collect(Collectors.toList());

        return new RecordBook(courseTitle, localDate, topics);
    }

    private static TopicScore createTopic(Topic topic) {
        String topicTitle = topic.getTitle();
        Duration durationInDays = Duration.ofDays((long) Math.ceil(topic.getDurationInHours() / TEACHING_HOURS_PER_DAY));
        return new TopicScore(topicTitle, durationInDays);
    }
}
