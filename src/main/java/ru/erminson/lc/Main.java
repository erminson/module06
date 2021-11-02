package ru.erminson.lc;

import lombok.extern.slf4j.Slf4j;
import ru.erminson.lc.model.dto.report.StudentReport;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.service.StudyService;
import ru.erminson.lc.utils.StudyServiceFactory;
import ru.erminson.lc.view.View;
import ru.erminson.lc.view.impl.ConsoleView;

import java.util.List;

@Slf4j
public class Main {
    private final View view;
    private final StudyService studyService;

    public Main() {
        view = new ConsoleView();
        studyService = StudyServiceFactory.createStudyService();
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.fakeBusinessActivities();
    }

    private static void printLine() {
        log.debug("-----------------------------------------------------------------");
    }

    private void fakeBusinessActivities() {
        List<Student> studentsOnCourses = studyService.getAllStudentsOnCourses();

        for (Student student : studentsOnCourses) {
            StudentReport report = studyService.getStudentReportByStudentName(student.getName());
            view.printStudentReport(report);
        }

        printLine();

        String studentName = "Lev";
        String courseTitle = "Course1";
        studyService.addStudentByName(studentName);
        studyService.enrollStudentOnCourse(studentName, courseTitle);

        StudentReport studentReportLev = studyService.getStudentReportByStudentName(studentName);
        view.printStudentReport(studentReportLev);

        printLine();

        studentName = "Ivan";
        StudentReport report = studyService.getStudentReportByStudentName(studentName);
        view.printStudentReport(report);
        studyService.rateTopic(studentName, "topic15", 90);
        report = studyService.getStudentReportByStudentName(studentName);
        view.printStudentReport(report);

        printLine();

        // Save report using single thread
        Runnable runnable = () -> studyService.saveStudentReports();
        measureElapsedTime(runnable);

        // Save report using single thread
        Runnable runnable2 = () -> studyService.saveStudentReportsWithMultithreading();
        measureElapsedTime(runnable2);
    }

    void measureElapsedTime(Runnable runnable) {
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        log.info("Execution time in milliseconds: {}", timeElapsed / 1000000);
    }
}
