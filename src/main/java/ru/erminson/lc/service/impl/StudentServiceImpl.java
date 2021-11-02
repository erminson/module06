package ru.erminson.lc.service.impl;

import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean addStudent(String name) {
        return studentRepository.addStudent(name);

    }

    @Override
    public Student getStudentByName(String name) {
        try {
            return studentRepository.getStudentByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public boolean removeStudent(String name) {
        try {
            return studentRepository.removeStudent(name);
        } catch (IllegalInitialDataException e) {
            return false;
        }
    }
}
