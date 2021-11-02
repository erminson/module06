package ru.erminson.lc.repository.impl;

import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {
    private final List<Student> students;

    public StudentRepositoryImpl() {
        this.students = new ArrayList<>();
    }

    public StudentRepositoryImpl(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean addStudent(String name) {
        Student student = new Student(name);
        if (students.contains(student)) {
            return false;
        }
        return students.add(student);
    }

    @Override
    public boolean removeStudent(String name) throws IllegalInitialDataException {
        Student student = getStudentByName(name);
        return students.remove(student);
    }

    @Override
    public Student getStudentByName(String name) throws IllegalInitialDataException {
        return students.stream()
                .filter(student -> student.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalInitialDataException::new);
    }

    @Override
    public List<Student> getAllStudents() {
        return students;
    }

    @Override
    public boolean isExistsStudent(String name) {
        return students.stream()
                .map(Student::getName)
                .anyMatch(n -> n.equals(name));
    }
}
