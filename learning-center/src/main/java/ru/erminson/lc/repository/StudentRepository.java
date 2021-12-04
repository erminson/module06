package ru.erminson.lc.repository;

import ru.erminson.lc.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    boolean save(String name);
    List<Student> findAll();
    Optional<Student> findById(long id);
    Optional<Student> findByName(String name);
    boolean deleteById(long id);
    boolean deleteByName(String name);
}
