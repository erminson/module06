package ru.erminson.lc.repository;

import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.IllegalInitialDataException;

import java.util.List;

public interface StudentRepository {
    boolean addStudent(String name);
    boolean removeStudent(String name) throws IllegalInitialDataException;
    Student getStudentByName(String name) throws IllegalInitialDataException;
    List<Student> getAllStudents();
    boolean isExistsStudent(String name);
}
