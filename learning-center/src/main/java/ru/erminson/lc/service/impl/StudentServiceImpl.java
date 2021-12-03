package ru.erminson.lc.service.impl;

import org.springframework.stereotype.Service;
import ru.erminson.lc.model.exception.EntityNotFoundException;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.service.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean add(String name) {
        return studentRepository.save(name);
    }

    @Override
    public Student findById(long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElseThrow(() -> new EntityNotFoundException(Student.class, "id", String.valueOf(id)));
    }

    @Override
    public Student findByName(String name) {
        Optional<Student> student = studentRepository.findByName(name);
        return student.orElseThrow(() -> new EntityNotFoundException(Student.class, "name", name));
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteByName(String name) {
        studentRepository.deleteByName(name);
    }
}
