package ru.erminson.lc.model.dto.report;

import java.util.List;

public class StudentReport {
    final String reportDate;
    final String studentName;
    final String courseTitle;
    final String startDate;
    final String endDate;
    final double average;
    final boolean ability;
    final List<TopicScoreReport> topicScores;

    StudentReport(StudentReportBuilder builder) {
        this.reportDate = builder.getReportDate();
        this.studentName = builder.getStudentName();
        this.courseTitle = builder.getCourseTitle();
        this.startDate = builder.getStartDate();
        this.endDate = builder.getEndDate();
        this.average = builder.getAverage();
        this.ability = builder.getAbility();
        this.topicScores = builder.getTopicScores();
    }

    public String getReportDate() {
        return  reportDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getAverage() {
        return average;
    }

    public boolean getAbility() {
        return ability;
    }

    public List<TopicScoreReport> getTopicScores() {
        return topicScores;
    }

    @Override
    public String toString() {
        return "StudentReport{" +
                "reportDate='" + reportDate + '\'' +
                ", studentName='" + studentName + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", average=" + average +
                ", ability=" + ability +
                ", topicScores=" + topicScores +
                '}';
    }
}
