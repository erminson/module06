package ru.erminson.lc.service.impl;

import org.springframework.stereotype.Service;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;
import ru.erminson.lc.model.exception.EntityNotFoundException;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.service.CourseService;
import ru.erminson.lc.service.RecordBookService;
import ru.erminson.lc.utils.RecordBookInitializer;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class RecordBookServiceImpl implements RecordBookService {
    private final RecordBookRepository recordBookRepository;
    private final CourseService courseService;

    public RecordBookServiceImpl(RecordBookRepository recordBookRepository, CourseService courseService) {
        this.recordBookRepository = recordBookRepository;
        this.courseService = courseService;
    }

    @Override
    public boolean enrollStudentOnCourse(Student student, Course course) {
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(course);
        return recordBookRepository.addStudentWithRecordBook(student, recordBook);
    }

    @Override
    public void enrollStudentOnCourse(long studentId, long courseId) {
        Course course = courseService.findById(courseId);
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(course);
        boolean result = recordBookRepository.enrollStudentOnCourse(studentId, recordBook);
        if (!result) {
            throw new EntityNotFoundException(Student.class, "studentId", String.valueOf(studentId));
        }
    }

    @Override
    public RecordBook getRecordBookByStudentName(String studentName) {
        return recordBookRepository.getRecordBook(studentName);
    }

    @Override
    public RecordBook getRecordBookByStudent(Student student) {
        return recordBookRepository.getRecordBook(student);
    }

    @Override
    public RecordBook getRecordBookByStudent(long studentId) {
        Optional<RecordBook> recordBook = recordBookRepository.findByStudentId(studentId);

        return recordBook.orElseThrow(() -> new EntityNotFoundException(RecordBook.class, "studentId", String.valueOf(studentId)));
    }

    @Override
    public boolean dismissStudentFromCourse(Student student) {
        return recordBookRepository.removeStudentFromCourse(student);
    }

    @Override
    public List<Student> getAllStudentsOnCourses() {
        return recordBookRepository.getAllStudents();
    }

    @Override
    public boolean rateTopic(TopicScore topicScore, int score) {
        return recordBookRepository.rateTopic(topicScore, score);
    }

    @Override
    public void rateTopic(long topicScoreId, int score) {
        boolean result = recordBookRepository.rateTopic(topicScoreId, score);
        if (!result) {
            throw new EntityNotFoundException(TopicScore.class, "topicScoreId", String.valueOf(topicScoreId));
        }
    }

    @Override
    public int getNumberRatedTopics(Student student) {
        RecordBook recordBook = getRecordBookByStudent(student);
        if (recordBook == null) {
            return 0;
        }

        return getNumberRatedTopics(recordBook);
    }

    @Override
    public int getNumberRatedTopics(RecordBook recordBook) {
        return (int) recordBook.getTopics().stream()
                .filter(topicScore -> topicScore.getScore() != 0)
                .count();
    }

    @Override
    public int getNumberTopics(Student student) {
        RecordBook recordBook = getRecordBookByStudent(student);

        return getNumberTopics(recordBook);
    }

    @Override
    public int getNumberTopics(RecordBook recordBook) {
        return recordBook.getTopics().size();
    }

    @Override
    public int getDaysUntilEndOfCourseByStudent(Student student, LocalDate nowDate) {
        RecordBook recordBook = getRecordBookByStudent(student);
        return getDaysUntilEndOfCourseByStudent(recordBook, nowDate);
    }

    @Override
    public int getDaysUntilEndOfCourseByStudent(RecordBook recordBook, LocalDate nowDate) {
        LocalDate endOfCourseDate = recordBook.getEndDate();

        if (nowDate.isAfter(endOfCourseDate)) {
            return 0;
        }

        Period endOfCoursePeriod = Period.between(nowDate, endOfCourseDate);

        return endOfCoursePeriod.getDays();
    }
}
