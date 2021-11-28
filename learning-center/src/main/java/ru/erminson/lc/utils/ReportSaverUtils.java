package ru.erminson.lc.utils;

import lombok.extern.slf4j.Slf4j;
import ru.erminson.lc.model.dto.report.StudentReport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class ReportSaverUtils {
    private static final String DATE_TEMPLATE = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TEMPLATE);

    private static final String REPORTS_DIRECTORY_NAME = "reports";
    private static final String REPORT_FILE_NAME_TEMPLATE = "students_report_(%s)";

    private static final String NUMBER_HEADER = "#";
    private static final String NAME_HEADER = "Name";
    private static final String COURSE_HEADER = "Course";
    private static final String DATE_HEADER = "Date";
    private static final String AVR_HEADER = "AVR";
    private static final String ABILITY_HEADER = "Ability";

    private static int numberIndents = 3;
    private static int nameIndents = NAME_HEADER.length();
    private static int courseIndents = COURSE_HEADER.length();
    private static int dateIndents = Math.max(DATE_TEMPLATE.length(), DATE_HEADER.length());
    private static int avrIndents = Math.max(AVR_HEADER.length(), 5);
    private static int abilityIndents = ABILITY_HEADER.length();

    private static final int DEFAULT_MAX_LENGTH_STUDENT_NAME = 10;
    private static final int DEFAULT_MAX_LENGTH_COURSE_TITLE = 10;

    private ReportSaverUtils() {
        throw new IllegalStateException("ReportSaver utils class");
    }

    public static void saveStudentReports(List<StudentReport> reports) {
        nameIndents = Math.max(nameIndents, getMaxLengthStudentName(reports));
        courseIndents = Math.max(courseIndents, getMaxLengthCourseTitle(reports));

        // n name course start average ability
        String headerTemplate = getHeaderTemplate();
        String dataTemplate = getDataTemplate();

        String header = getHeader(headerTemplate);
        StringBuilder sb = new StringBuilder(header);

        int counter = 1;
        for (StudentReport report : reports) {
            String inlineReport = getLineData(report, counter, dataTemplate);
            sb.append(inlineReport);
            counter++;
        }

        save(sb.toString());
    }

    private static int getMaxLengthStudentName(List<StudentReport> reports) {
        return reports.stream()
                .map(StudentReport::getStudentName)
                .mapToInt(String::length)
                .max()
                .orElse(DEFAULT_MAX_LENGTH_STUDENT_NAME);
    }

    private static int getMaxLengthCourseTitle(List<StudentReport> reports) {
        return reports.stream()
                .map(StudentReport::getCourseTitle)
                .mapToInt(String::length)
                .max()
                .orElse(DEFAULT_MAX_LENGTH_COURSE_TITLE);
    }

    private static String getHeaderTemplate() {
        return "%" + numberIndents + "s " +
                "%-" + nameIndents + "s " +
                "%-" + courseIndents + "s " +
                "%-" + dateIndents + "s " +
                "%-" + avrIndents + "s " +
                "%" + abilityIndents + "s%n";
    }

    private static String getDataTemplate() {
        return "%" + numberIndents + "d " +
                "%-" + nameIndents + "s " +
                "%-" + courseIndents + "s " +
                "%" + dateIndents + "s " +
                "%3.1f " +
                "%" + abilityIndents + "b%n";
    }

    private static String getHeader(String headerTemplate) {
        return String.format(
                headerTemplate,
                NUMBER_HEADER,
                NAME_HEADER,
                COURSE_HEADER,
                DATE_HEADER,
                AVR_HEADER,
                ABILITY_HEADER
        );
    }

    private static String getLineData(StudentReport report, int index, String dataTemplate) {
        return String.format(
                dataTemplate,
                index,
                report.getStudentName(),
                report.getCourseTitle(),
                report.getStartDate(),
                report.getAverage(),
                report.getAbility()
        );
    }

    private static void save(String reportString) {
        log.debug("\n{}", reportString);
        String reportDate = LocalDate.now().format(FORMATTER);

        File directory = new File(REPORTS_DIRECTORY_NAME);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File reportFile = new File(REPORTS_DIRECTORY_NAME, String.format(REPORT_FILE_NAME_TEMPLATE, reportDate));
        try (FileWriter fileWriter = new FileWriter(reportFile)) {
            fileWriter.write(reportString);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
