package ru.erminson.lc.service;

import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;

import java.time.LocalDate;
import java.util.List;

public interface RecordBookService {
    boolean enrollStudentOnCourse(Student student, Course course);
    RecordBook getRecordBookByStudent(Student student);
    boolean dismissStudentFromCourse(Student student);
    List<Student> getAllStudentsOnCourses();
    boolean rateTopic(TopicScore topicScore, int score);
    int getNumberRatedTopics(Student student);
    int getNumberTopics(Student student);

    int getDaysUntilEndOfCourseByStudent(Student student, LocalDate nowDate);
}
