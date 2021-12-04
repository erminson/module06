package ru.erminson.lc.service;

import ru.erminson.lc.model.dto.request.StudentRequest;
import ru.erminson.lc.model.entity.Student;

import java.util.List;

public interface StudentService {
    boolean add(String name);
    Student add(StudentRequest student);
    Student findById(long id);
    Student findByName(String name);
    List<Student> findAll();
    void deleteById(long id);
    void deleteByName(String name);
}
