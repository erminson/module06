package ru.erminson.lc.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.StudentRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

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
        int updateCount = jdbcTemplate.update(ADD_STUDENT_SQL, name);
        return updateCount > 0;
    }

    @Override
    public boolean removeStudent(String name) throws IllegalInitialDataException {
        int deleteCount = jdbcTemplate.update(DELETE_STUDENT_BY_NAME_SQL, name);
        return deleteCount > 0;
    }

    @Override
    public Student getStudentByName(String name) throws IllegalInitialDataException {
        Student student = jdbcTemplate.queryForObject(GET_STUDENT_BY_NAME_SQL, new StudentRowMapper(), name);
        if (Objects.isNull(student)) {
            throw new IllegalInitialDataException(String.format("Student '%s' not found", name));
        }

        return student;
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
