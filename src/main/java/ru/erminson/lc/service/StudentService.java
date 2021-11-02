package ru.erminson.lc.service;

import ru.erminson.lc.model.entity.Student;

import java.util.List;

public interface StudentService {
    boolean addStudent(String name);
    Student getStudentByName(String name);
    List<Student> getAllStudents();
    boolean removeStudent(String name);
}
