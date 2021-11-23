package ru.erminson.lc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.erminson.lc.configuration.TopicScoreConfig;
import ru.erminson.lc.model.dto.report.StudentReport;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;
import ru.erminson.lc.service.StudyService;
import ru.erminson.lc.service.impl.StudyServiceImpl;
import ru.erminson.lc.view.View;
import ru.erminson.lc.view.impl.ConsoleView;

import java.util.List;

@Slf4j
@EnableConfigurationProperties({TopicScoreConfig.class})
@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private final AnnotationConfigApplicationContext context;

    @Autowired
    public Main(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {
        TopicScore myTopic = context.getBean(TopicScore.class);
        log.info("Topic: {}", myTopic);

        View view = new ConsoleView();

        StudyService studyService = context.getBean("studyServiceImpl", StudyServiceImpl.class);

        fakeBusinessActivities(studyService, view);

        context.close();
    }

    private static void printLine() {
        log.debug("-----------------------------------------------------------------");
    }

    private static void fakeBusinessActivities(StudyService studyService, View view) {
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

        String temporaryStudentName = "Michael";
        studyService.addStudentByName(temporaryStudentName);
        studyService.enrollStudentOnCourse(temporaryStudentName, courseTitle);
        studyService.dismissStudentFromCourse(temporaryStudentName);
        studyService.removeStudentByName("Michael");

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
        Runnable runnable = studyService::saveStudentReports;
        measureElapsedTime(runnable);

        // Save report using single thread
        Runnable runnable2 = studyService::saveStudentReportsWithMultithreading;
        measureElapsedTime(runnable2);
    }

    private static void measureElapsedTime(Runnable runnable) {
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        log.info("Execution time in milliseconds: {}", timeElapsed / 1000000);
    }
}
