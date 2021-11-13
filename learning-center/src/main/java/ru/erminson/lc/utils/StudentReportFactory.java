package ru.erminson.lc.utils;

import ru.erminson.lc.model.dto.report.StudentReport;
import ru.erminson.lc.model.dto.report.StudentReportBuilder;
import ru.erminson.lc.model.dto.report.TopicScoreReport;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentReportFactory {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private StudentReportFactory() {
        throw new IllegalStateException("StudentReportFactory utility class");
    }

    public static StudentReport create(Student student, RecordBook recordBook, boolean ability) {
        String reportDate = LocalDate.now().format(FORMATTER);
        String name = student.getName();
        String courseTitle = recordBook.getCourseTitle();
        String startDate = recordBook.getStartDate().format(FORMATTER);
        String endDate = recordBook.getEndDate().format(FORMATTER);
        List<TopicScoreReport> topicsScore = createTopicScoreList(recordBook.getStartDate(), recordBook.getTopics());
        double average = recordBook.getAverageScore();

        return new StudentReportBuilder()
                .reportDate(reportDate)
                .studentName(name)
                .courseTitle(courseTitle)
                .startDate(startDate)
                .endDate(endDate)
                .topicScores(topicsScore)
                .average(average)
                .ability(ability)
                .build();
    }

    private static List<TopicScoreReport> createTopicScoreList(LocalDate startDate, List<TopicScore> topicScores) {
        LocalDate currentDate = startDate;
        List<TopicScoreReport> topicScoreReports = new ArrayList<>();
        for (final TopicScore topicScore: topicScores) {
            LocalDate endDate = currentDate.plusDays(topicScore.getDurationInDays().toDays());
            TopicScoreReport topicScoreReport = new TopicScoreReport(
                    topicScore.getTopicTitle(),
                    currentDate.format(FORMATTER),
                    endDate.format(FORMATTER),
                    topicScore.getScore()
            );

            topicScoreReports.add(topicScoreReport);
            currentDate = endDate;
        }

        return topicScoreReports;
    }
}
