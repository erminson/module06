package ru.erminson.lc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.erminson.lc.model.dto.report.StudentReport;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;
import ru.erminson.lc.service.CourseService;
import ru.erminson.lc.service.RecordBookService;
import ru.erminson.lc.service.StudentService;
import ru.erminson.lc.service.StudyService;
import ru.erminson.lc.utils.ReportSaverUtils;
import ru.erminson.lc.utils.StudentComparatorFactory;
import ru.erminson.lc.utils.StudentReportFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudyServiceImpl implements StudyService {
    private static final int PASSING_SCORE = 75;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 100;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final StudentService studentService;
    private final RecordBookService recordBookService;
    private final CourseService courseService;

    @Autowired
    public StudyServiceImpl(
            StudentService studentService,
            RecordBookService recordBookService,
            CourseService courseService
    ) {
        this.studentService = studentService;
        this.recordBookService = recordBookService;
        this.courseService = courseService;
    }

    @Override
    public boolean addStudentByName(String name) {
        return studentService.add(name);
    }

    @Override
    public boolean removeStudentByName(String name) {
        Student student = studentService.findByName(name);
        recordBookService.dismissStudentFromCourse(student);
        studentService.deleteByName(name);

        return true;
    }

    @Override
    public boolean enrollStudentOnCourse(String name, String courseTitle) {
        Student student = studentService.findByName(name);
        Course course = courseService.getCourseByTitle(courseTitle);
        if (student == null || course == null) {
            log.error("Student or record book not found: {} {}", name, courseTitle);
            return false;
        }

        return recordBookService.enrollStudentOnCourse(student, course);
    }

    @Override
    public boolean dismissStudentFromCourse(String name) {
        Student student = studentService.findByName(name);
        if (student == null) {
            log.error("Student not found: {}", name);
            return false;
        }

        return recordBookService.dismissStudentFromCourse(student);
    }

    @Override
    public boolean rateTopic(String name, String topicTitle, int score, LocalDate nowDate) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            return false;
        }

        Student student = studentService.findByName(name);
        // TODO: Create method for getting recordbook by studentId/studentName
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        if (student == null || recordBook == null) {
            log.error("Student or record book not found: {} {}", name, topicTitle);
            return false;
        }

        if (recordBook.isExistsTopic(topicTitle)) {
            LocalDate endDate = recordBook.getEndDateTopic(topicTitle);
            if (endDate.isAfter(nowDate)) {
                // TODO: Add log.error
                return false;
            }

            TopicScore topicScore = recordBook.getTopicScoreByTitle(topicTitle);
            return recordBookService.rateTopic(topicScore, score);
        }

        return false;
    }

    @Override
    public boolean rateTopic(String name, String topicTitle, int score) {
        return rateTopic(name, topicTitle, score, LocalDate.now());
    }

    @Override
    public RecordBook getRecordBookByStudentName(String name) {
        return recordBookService.getRecordBookByStudentName(name);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @Override
    public List<Student> getAllStudentsOnCourses() {
        return recordBookService.getAllStudentsOnCourses();
    }

    @Override
    public List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending) {
        return getAllStudentsOnCoursesSortedBy(sortType, ascending, LocalDate.now());
    }

    @Override
    public List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending, LocalDate nowDate) {
        List<Student> students = recordBookService.getAllStudentsOnCourses();

        Comparator<Student> comparator;
        switch (sortType) {
            case AVR:
                comparator = StudentComparatorFactory.getAverageComparator(recordBookService);
                break;
            case DAYS:
                comparator = StudentComparatorFactory.getDaysUntilEndOfCourseComparator(recordBookService, nowDate);
                break;
            case START:
                comparator = StudentComparatorFactory.getStartDateComparator(recordBookService);
                break;
            case COURSE:
                comparator = StudentComparatorFactory.getCourseComparator(recordBookService);
                break;
            default:
                comparator = StudentComparatorFactory.getStudentNameComparator();
        }

        if (ascending) {
            students.sort(comparator);
        } else {
            students.sort(comparator.reversed());
        }

        return students;
    }

    @Override
    public List<Student> getAllStudentsAbilityToCompleteCourse(LocalDate nowDate) {
        List<Student> students = recordBookService.getAllStudentsOnCourses();

        return students.stream()
                .filter(student -> canStudentCompleteCourseByStudentName(student, nowDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> getAllStudentsOutCourses() {
        List<Student> students = getAllStudents();
        List<Student> studentsOnCourses = getAllStudentsOnCourses();

        return students.stream()
                .filter(student -> !studentsOnCourses.contains(student))
                .collect(Collectors.toList());
    }

    @Override
    public int getDaysUntilEndOfCourseByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.findByName(name);
        if (student == null) {
            return 0;
        }

        return recordBookService.getDaysUntilEndOfCourseByStudent(student, nowDate);
    }

    @Override
    public int getDaysUntilEndOfCourseByStudentName(String name) {
        return getDaysUntilEndOfCourseByStudentName(name, LocalDate.now());
    }

    private boolean canStudentCompleteCourseByStudentName(RecordBook recordBook, LocalDate nowDate) {
        int daysLeft = recordBookService.getDaysUntilEndOfCourseByStudent(recordBook, nowDate);

        if (daysLeft == 0) {
            return false;
        }

        int numberTopicsLeftBeRated =
                recordBookService.getNumberTopics(recordBook) - recordBookService.getNumberRatedTopics(recordBook);
        if (daysLeft < numberTopicsLeftBeRated) {
            return false;
        }

        return recordBook.getAverageScoreInBestCase() >= PASSING_SCORE;
    }

    private boolean canStudentCompleteCourseByStudentName(Student student, LocalDate nowDate) {
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        return canStudentCompleteCourseByStudentName(recordBook, nowDate);
    }

    @Override
    public boolean canStudentCompleteCourseByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.findByName(name);
        if (student == null) {
            log.error("Student not found: {}", name);
            return false;
        }

        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        // TODO: check recordBook for null

        return canStudentCompleteCourseByStudentName(recordBook, nowDate);
    }

    @Override
    public boolean canStudentCompleteCourseByStudentName(String name) {
        return canStudentCompleteCourseByStudentName(name, LocalDate.now());
    }


    public StudentReport getStudentReportByStudentAndDate(Student student, LocalDate nowDate) {
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        boolean ability = canStudentCompleteCourseByStudentName(recordBook, nowDate);

        return StudentReportFactory.create(student, recordBook, ability);
    }

    public StudentReport getStudentReportByStudent(Student student) {
        return getStudentReportByStudentAndDate(student, LocalDate.now());
    }

    @Override
    public StudentReport getStudentReportByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.findByName(name);
        // TODO: Exception
        if (student == null) {
            log.error("Student not found: {}", name);
            return null;
        }

        return getStudentReportByStudentAndDate(student, nowDate);
    }

    @Override
    public StudentReport getStudentReportByStudentName(String name) {
        return getStudentReportByStudentName(name, LocalDate.now());
    }

    @Override
    public void saveStudentReports(LocalDate nowDate) {
        List<Student> students = getAllStudentsOnCourses();
        List<StudentReport> reports = students.stream()
                .map(student -> getStudentReportByStudentAndDate(student, nowDate))
                .collect(Collectors.toList());

        ReportSaverUtils.saveStudentReports(reports);
    }

    @Override
    public void saveStudentReportsWithMultithreading() {
        List<Student> students = getAllStudentsOnCourses();
        List<Future<StudentReport>> futureReports = new ArrayList<>();
        try {
            for (Student student : students) {
                Future<StudentReport> futureReport = executorService.submit(
                        () -> getStudentReportByStudent(student)
                );
                futureReports.add(futureReport);
            }
        } finally {
            executorService.shutdown();
        }

        try {
//            executorService.awaitTermination(10, TimeUnit.SECONDS);

            List<StudentReport> reports = new ArrayList<>();
            for (Future<StudentReport> futureReport : futureReports) {
                StudentReport report = futureReport.get();
                reports.add(report);
            }

            ReportSaverUtils.saveStudentReports(reports);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void saveStudentReports() {
        saveStudentReports(LocalDate.now());
    }
}
