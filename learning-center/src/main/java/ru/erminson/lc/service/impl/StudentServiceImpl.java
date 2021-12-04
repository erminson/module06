package ru.erminson.lc.service.impl;

import org.springframework.stereotype.Service;
import ru.erminson.lc.model.dto.request.StudentRequest;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.EntityHasAlreadyBeenCreated;
import ru.erminson.lc.model.exception.EntityNotFoundException;
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
        if (!studentRepository.save(name)) {
            throw new EntityNotFoundException(String.class, "name", name);
        }
        return true;
    }

    @Override
    public Student add(StudentRequest studentRequest) {
        Optional<Student> studentOptional = studentRepository.save(studentRequest);
        return studentOptional
                .orElseThrow(() -> new EntityHasAlreadyBeenCreated(Student.class, "name", studentRequest.getName()));
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
    public void deleteById(long id) {
        boolean deleteResult = studentRepository.deleteById(id);
        if (!deleteResult) {
            throw  new EntityNotFoundException(Student.class, "id", String.valueOf(id));
        }
    }

    @Override
    public void deleteByName(String name) {
        studentRepository.deleteByName(name);
    }
}
