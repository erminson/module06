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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudyServiceImpl implements StudyService {
    private static final int PASSING_SCORE = 75;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 100;

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
        return studentService.addStudent(name);
    }

    @Override
    public boolean removeStudentByName(String name) {
        Student student = studentService.getStudentByName(name);
        recordBookService.dismissStudentFromCourse(student);
        studentService.removeStudent(name);

        return true;
    }

    @Override
    public boolean enrollStudentOnCourse(String name, String courseTitle) {
        Student student = studentService.getStudentByName(name);
        Course course = courseService.getCourseByTitle(courseTitle);
        if (student == null || course == null) {
            log.error("Student or record book not found: {} {}", name, courseTitle);
            return false;
        }

        return recordBookService.enrollStudentOnCourse(student, course);
    }

    @Override
    public boolean dismissStudentFromCourse(String name) {
        Student student = studentService.getStudentByName(name);
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

        Student student = studentService.getStudentByName(name);
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        if (student == null || recordBook == null) {
            log.error("Student or record book not found: {} {}", name, topicTitle);
            return false;
        }

        if (recordBook.isExistsTopic(topicTitle)) {
            LocalDate endDate = recordBook.getEndDateTopic(topicTitle);
            if (endDate.isAfter(nowDate)) {
                return false;
            }

            TopicScore topicScore = recordBook.getTopicScoreByTitle(topicTitle);
            topicScore.setScore(score);
            return true;
        }

        return false;
    }

    @Override
    public boolean rateTopic(String name, String topicTitle, int score) {
        return rateTopic(name, topicTitle, score, LocalDate.now());
    }

    @Override
    public RecordBook getRecordBookByStudentName(String name) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            log.error("Student not found: {}", name);
            return null;
        }

        return recordBookService.getRecordBookByStudent(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
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
                .filter(student -> canStudentCompleteCourseByStudentName(student.getName(), nowDate))
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
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            return 0;
        }

        return recordBookService.getDaysUntilEndOfCourseByStudent(student, nowDate);
    }

    @Override
    public int getDaysUntilEndOfCourseByStudentName(String name) {
        return getDaysUntilEndOfCourseByStudentName(name, LocalDate.now());
    }

    @Override
    public boolean canStudentCompleteCourseByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            log.error("Student not found: {}", name);
            return false;
        }

        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        // TODO: check recordBook for null

        int daysLeft = getDaysUntilEndOfCourseByStudentName(student.getName(), nowDate);
        if (daysLeft == 0) {
            return false;
        }

        int numberTopicsLeftBeRated =
                recordBookService.getNumberTopics(student) - recordBookService.getNumberRatedTopics(student);
        if (daysLeft < numberTopicsLeftBeRated) {
            return false;
        }

        return recordBook.getAverageScoreInBestCase() >= PASSING_SCORE;
    }

    @Override
    public boolean canStudentCompleteCourseByStudentName(String name) {
        return canStudentCompleteCourseByStudentName(name, LocalDate.now());
    }

    @Override
    public StudentReport getStudentReportByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            log.error("Student not found: {}", name);
            return null;
        }

        //
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        boolean ability = canStudentCompleteCourseByStudentName(name, nowDate);
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);

        return StudentReportFactory.create(student, recordBook, ability);
    }

    @Override
    public StudentReport getStudentReportByStudentName(String name) {
        return getStudentReportByStudentName(name, LocalDate.now());
    }

    @Override
    public void saveStudentReports(LocalDate nowDate) {
        List<Student> students = getAllStudentsOnCourses();
        List<StudentReport> reports = students.stream()
                .map(student -> getStudentReportByStudentName(student.getName(), nowDate))
                .collect(Collectors.toList());

        ReportSaverUtils.saveStudentReports(reports);
    }

    @Override
    public void saveStudentReportsWithMultithreading() {
        List<Student> students = getAllStudentsOnCourses();
        ExecutorService executorService = Executors.newFixedThreadPool(students.size());
        List<Future<StudentReport>> futureReports = new ArrayList<>();
        try {
            for (Student student : students) {
                Future<StudentReport> futureReport = executorService.submit(
                        () -> getStudentReportByStudentName(student.getName())
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
