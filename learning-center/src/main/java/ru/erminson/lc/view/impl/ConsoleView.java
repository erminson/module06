package ru.erminson.lc.view.impl;

import lombok.extern.slf4j.Slf4j;
import ru.erminson.lc.model.dto.report.StudentReport;
import ru.erminson.lc.model.dto.report.TopicScoreReport;
import ru.erminson.lc.view.View;

import java.util.List;

@Slf4j
public class ConsoleView implements View {
    private static final int NUMBER_FIXED_SYMBOLS = 25;
    private static final int NUMBER_SPACES = 3;
    private static final int COURSE_WIDTH = 6;

    private static String getHeaderBlock(StudentReport studentReport) {
        String headerTemplate = "%10s: %s%n";

        StringBuilder sb = new StringBuilder("\n");
        sb.append(String.format("Student report (%s)%n", studentReport.getReportDate()));
        sb.append(String.format(headerTemplate, "Name", studentReport.getStudentName()));
        sb.append(String.format(headerTemplate, "Course", studentReport.getCourseTitle()));
        sb.append(String.format("%10s: %.1f%n", "Average", studentReport.getAverage()));
        sb.append(String.format(headerTemplate, "Start Date", studentReport.getStartDate()));
        sb.append(String.format(headerTemplate, "End Date", studentReport.getEndDate()));
        sb.append(String.format(headerTemplate, "Ability", studentReport.getAbility()));

        return sb.toString();
    }

    private static String getTableBlock(StudentReport studentReport, int maxCourseIndents) {
        StringBuilder sb = new StringBuilder("\n");

        sb.append(getHorizontalLine(maxCourseIndents));

        String tableTemplate =
                "%-10s %-10s %-" + maxCourseIndents + "s %5s%n";
        sb.append(String.format(tableTemplate, "Start", "End", "Title", "Score"));

        sb.append(getHorizontalLine(maxCourseIndents));

        List<TopicScoreReport> topicScores = studentReport.getTopicScores();

        topicScores.forEach(
                topicScore -> sb.append(String.format(
                                tableTemplate,
                                topicScore.getStartDate(),
                                topicScore.getEndDate(),
                                topicScore.getTopicTitle(),
                                topicScore.getScore()
                        )
                )
        );

        return sb.toString();
    }

    private static String getHorizontalLine(int indents) {
        return "-".repeat(NUMBER_FIXED_SYMBOLS + NUMBER_SPACES + indents) + "\n";
    }

    @Override
    public void printStudentReport(StudentReport studentReport) {
        log.info(getHeaderBlock(studentReport));

        List<TopicScoreReport> topicScores = studentReport.getTopicScores();
        int maxIndents = topicScores.stream()
                .map(TopicScoreReport::getTopicTitle)
                .mapToInt(String::length)
                .max()
                .orElse(10);

        int maxCourseIndents = Math.max(COURSE_WIDTH, maxIndents);

        log.info(getTableBlock(studentReport, maxCourseIndents));
    }
}
