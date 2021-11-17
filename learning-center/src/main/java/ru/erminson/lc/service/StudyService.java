package ru.erminson.lc.service;

import ru.erminson.lc.model.dto.report.StudentReport;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;

import java.time.LocalDate;
import java.util.List;

public interface StudyService {
    enum SortType { AVR, DAYS, NAME, START, COURSE }

    boolean addStudentByName(String name);

    boolean removeStudentByName(String name);

    boolean enrollStudentOnCourse(String name, String courseTitle);

    boolean dismissStudentFromCourse(String name);

    boolean rateTopic(String name, String topicTitle, int score, LocalDate nowDate);

    boolean rateTopic(String name, String topicTitle, int score);

    RecordBook getRecordBookByStudentName(String name);

    List<Student> getAllStudents();

    List<Student> getAllStudentsOnCourses();

    List<Student> getAllStudentsOutCourses();

    List<Student> getAllStudentsAbilityToCompleteCourse(LocalDate nowDate);

    List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending);

    List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending, LocalDate nowDate);

    int getDaysUntilEndOfCourseByStudentName(String name);

    int getDaysUntilEndOfCourseByStudentName(String name, LocalDate nowDate);

    boolean canStudentCompleteCourseByStudentName(String name);

    boolean canStudentCompleteCourseByStudentName(String name, LocalDate nowDate);

    StudentReport getStudentReportByStudentName(String name, LocalDate nowDate);

    StudentReport getStudentReportByStudentName(String name);

    void saveStudentReports();

    void saveStudentReports(LocalDate nowDate);

    void saveStudentReportsWithMultithreading();
}
