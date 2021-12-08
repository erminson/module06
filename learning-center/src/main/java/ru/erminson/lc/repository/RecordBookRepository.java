package ru.erminson.lc.repository;

import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;

import java.util.List;
import java.util.Optional;

public interface RecordBookRepository {
    boolean addStudentWithRecordBook(Student student, RecordBook recordBook);
    boolean rateTopic(TopicScore topicScore, int score);
    RecordBook getRecordBook(String studentName);
    RecordBook getRecordBook(Student student);
    Optional<RecordBook> findByStudentId(long studentId);
    boolean isStudentOnCourse(Student student);
    boolean removeStudentFromCourse(Student student);
    List<Student> getAllStudents();
}