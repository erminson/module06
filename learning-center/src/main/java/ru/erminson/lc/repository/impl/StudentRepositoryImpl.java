package ru.erminson.lc.repository.impl;

import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.logging.annotation.EnableCustomAOP;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StudentRepositoryImpl implements StudentRepository {
    private final List<Student> students;

    public StudentRepositoryImpl() {
        this.students = new ArrayList<>();
    }

    public StudentRepositoryImpl(List<Student> students) {
        this.students = students;
    }

    @Override
    @EnableCustomAOP
    public boolean save(String name) {
        Student student = new Student(name);
        if (students.contains(student)) {
            return false;
        }
        return students.add(student);
    }

    @Override
    public boolean deleteById(long id) {
        throw new UnsupportedOperationException("deleteById: not supported yet");
    }

    @Override
    public boolean deleteByName(String name) {
        Optional<Student> student = findByName(name);
        Student s = student.orElse(null);
        return Objects.nonNull(s) && students.remove(s);
    }

    @Override
    public Optional<Student> findById(long id) {
        throw new UnsupportedOperationException("findById: not supported yet");
    }

    @Override
    public Optional<Student> findByName(String name) {
        return students.stream()
                .filter(student -> student.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Student> findAll() {
        return students;
    }
}
