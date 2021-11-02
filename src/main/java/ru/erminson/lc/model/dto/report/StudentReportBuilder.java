package ru.erminson.lc.model.dto.report;

import java.util.List;

public class StudentReportBuilder {
    String reportDate;
    String studentName;
    String courseTitle;
    String startDate;
    String endDate;
    double average;
    boolean ability;
    List<TopicScoreReport> topicScores;

    public StudentReportBuilder reportDate(final String reportDate) {
        this.reportDate = reportDate;
        return this;
    }

    public StudentReportBuilder studentName(final String studentName) {
        this.studentName = studentName;
        return this;
    }

    public StudentReportBuilder courseTitle(final String courseTitle) {
        this.courseTitle = courseTitle;
        return this;
    }

    public StudentReportBuilder startDate(final String startDate) {
        this.startDate = startDate;
        return this;
    }

    public StudentReportBuilder endDate(final String endDate) {
        this.endDate = endDate;
        return this;
    }

    public StudentReportBuilder average(final double average) {
        this.average = average;
        return this;
    }

    public StudentReportBuilder ability(final boolean ability) {
        this.ability = ability;
        return this;
    }

    public StudentReportBuilder topicScores(final List<TopicScoreReport> topicScores) {
        this.topicScores = topicScores;
        return this;
    }

    public String getReportDate() {
        return reportDate;
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

    public StudentReport build() {
        return new StudentReport(this);
    }
}
