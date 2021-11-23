package ru.erminson.lc.repository.impl;

import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.TopicScore;
import ru.erminson.lc.repository.RecordBookRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordBookRepositoryImpl implements RecordBookRepository {

    private final Map<Student, RecordBook> storage;

    public RecordBookRepositoryImpl() {
        this.storage = new HashMap<>();
    }

    public RecordBookRepositoryImpl(Map<Student, RecordBook> storage) {
        this.storage = storage;
    }

    @Override
    public boolean addStudentWithRecordBook(Student student, RecordBook recordBook) {
        storage.putIfAbsent(student, recordBook);
        return true;
    }

    @Override
    public boolean rateTopic(TopicScore topicScore, int score) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public RecordBook getRecordBook(Student student) {
        return storage.get(student);
    }

    @Override
    public boolean isStudentOnCourse(Student student) {
        return storage.containsKey(student);
    }

    @Override
    public boolean removeStudentFromCourse(Student student) {
        storage.remove(student);
        return true;
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(storage.keySet());
    }
}
