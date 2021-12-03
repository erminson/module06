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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RecordBookInitializer {
    private RecordBookInitializer() {
        throw new IllegalStateException("RecordBookInitializer utility class");
    }

    public static RecordBook createRecordBookByCourseAndStartDate(long recordBookId, Course course, LocalDate startDate) {
        String courseTitle = course.getTitle();

        List<Topic> topics = course.getTopics();
        List<TopicScore> topicScores = IntStream.range(0, topics.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), topics::get))
                .entrySet().stream().map(e -> TopicScore.create((long)e.getKey() + 1, e.getValue()))
                .collect(Collectors.toList());

        return new RecordBook(recordBookId, course.getId(), courseTitle, startDate, topicScores);
    }


    public static RecordBook createRecordBookByCourseAndStartDate(Course course, LocalDate startDate) {
        return createRecordBookByCourseAndStartDate(0, course, startDate);
    }

    public static RecordBook createRecordBookByCourse(long recordBookId, Course course) {
        LocalDate startDate = LocalDate.now().plusDays(1);
        return createRecordBookByCourseAndStartDate(recordBookId, course, startDate);
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

        return new RecordBook(0, 0, courseTitle, localDate, topics);
    }
}
