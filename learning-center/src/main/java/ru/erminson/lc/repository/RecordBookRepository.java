package ru.erminson.lc.repository;

import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;

import java.util.List;

public interface RecordBookRepository {
    boolean addStudentWithRecordBook(Student student, RecordBook recordBook);
    boolean rateTopic(TopicScore topicScore, int score);
    RecordBook getRecordBook(Student student);
    boolean isStudentOnCourse(Student student);
    boolean removeStudentFromCourse(Student student);
    List<Student> getAllStudents();
}