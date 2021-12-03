package ru.erminson.lc.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.StudentRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class StudentRepositoryJdbc implements StudentRepository {
    private static final String ID_COLUMN = "ID";
    private static final String NAME_COLUMN = "NAME";

    private static final String GET_ALL_STUDENT_SQL = String.format(
            "SELECT %s, %s FROM STUDENT;",
            ID_COLUMN, NAME_COLUMN
    );
    private static final String ADD_STUDENT_SQL = "INSERT INTO STUDENT (NAME) VALUES (?)";
    private static final String DELETE_STUDENT_BY_NAME_SQL = "DELETE FROM STUDENT WHERE NAME = ?";
    private static final String GET_STUDENT_BY_NAME_SQL = "SELECT ID, NAME FROM STUDENT WHERE NAME = ?";

    private final JdbcTemplate jdbcTemplate;

    public StudentRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addStudent(String name) {
        try {
            jdbcTemplate.update(ADD_STUDENT_SQL, name);
            log.debug("Student: {} was added", name);
        } catch (DataIntegrityViolationException e) {
            log.error("Student: {} hasn't added. {}", name, e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean removeStudent(String name) throws IllegalInitialDataException {
        int deleteCount = jdbcTemplate.update(DELETE_STUDENT_BY_NAME_SQL, name);
        if (deleteCount == 0) {
            log.error("Student: {} hasn't removed.", name);
            throw new IllegalInitialDataException("Student: " + name + " doesn't exist");
        }

        log.debug("Student: {} was removed", name);
        return true;
    }

    @Override
    public Student getStudentByName(String name) throws IllegalInitialDataException {
        try {
            Student student = jdbcTemplate.queryForObject(GET_STUDENT_BY_NAME_SQL, new StudentRowMapper(), name);
            log.debug("Student: {} was found.", name);
            return student;
        } catch (EmptyResultDataAccessException e) {
            log.error("Student: {} wasn't found.", name);
            throw new IllegalInitialDataException(String.format("Student '%s' not found", name));
        }
    }

    @Override
    public List<Student> getAllStudents() {
        return jdbcTemplate.query(GET_ALL_STUDENT_SQL, new StudentRowMapper());
    }

    @Override
    public boolean isExistsStudent(String name) {
        try {
            getStudentByName(name);
        } catch (IllegalInitialDataException e) {
            return false;
        }

        return true;
    }

    private static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student student = new Student();
            student.setId(rs.getLong(ID_COLUMN));
            student.setName(rs.getString(NAME_COLUMN));

            return student;
        }
    }
}
