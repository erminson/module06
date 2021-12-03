package ru.erminson.lc.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.erminson.lc.mappers.StudentRowMapper;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class StudentRepositoryJdbc implements StudentRepository {
    private static final String GET_ALL_STUDENT_SQL = String.format(
            "SELECT %s, %s FROM STUDENT;",
            StudentRowMapper.ID_COLUMN, StudentRowMapper.NAME_COLUMN
    );
    private static final String ADD_STUDENT_SQL = "INSERT INTO STUDENT (NAME) VALUES (?)";
    private static final String DELETE_STUDENT_BY_NAME_SQL = "DELETE FROM STUDENT WHERE NAME = ?";
    private static final String GET_STUDENT_BY_NAME_SQL = "SELECT ID, NAME FROM STUDENT WHERE NAME = ?";
    private static final String GET_STUDENT_BY_ID_SQL = "SELECT id, name FROM student WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper;

    public StudentRepositoryJdbc(JdbcTemplate jdbcTemplate, StudentRowMapper studentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentRowMapper = studentRowMapper;
    }

    @Override
    public boolean save(String name) {
        try {
            jdbcTemplate.update(ADD_STUDENT_SQL, name);
            log.debug("Student: {} was added", name);
            return true;
        } catch (DataIntegrityViolationException e) {
            log.debug("Student: {} wasn't added. {}", name, e.getMessage());
            return false;
        }
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(GET_ALL_STUDENT_SQL, studentRowMapper);
    }

    @Override
    public Optional<Student> findById(long id) {
        Student student = null;
        try {
            student = jdbcTemplate.queryForObject(GET_STUDENT_BY_ID_SQL, studentRowMapper, id);
            log.debug("Student with id: {} was found", id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Student with id: {} wasn't found", id);
        }

        return Optional.ofNullable(student);
    }

    @Override
    public Optional<Student> findByName(String name) {
        Student student = null;
        try {
            student = jdbcTemplate.queryForObject(GET_STUDENT_BY_NAME_SQL, studentRowMapper, name);
            log.debug("Student with name: {} was found", name);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Student with name: {} wasn't found", name);
        }

        return Optional.ofNullable(student);
    }

    @Override
    public boolean deleteByName(String name) {
        int deleteCount = jdbcTemplate.update(DELETE_STUDENT_BY_NAME_SQL, name);
        boolean deleteResult = deleteCount != 0;
        log.debug("Delete student by name: {}. Result: {}", name, deleteResult);
        return deleteResult;
    }
}
