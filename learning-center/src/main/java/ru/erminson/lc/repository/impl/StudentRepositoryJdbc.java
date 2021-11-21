package ru.erminson.lc.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.StudentRepository;

import java.util.List;

@Repository("studentRepositoryJdbc")
public class StudentRepositoryJdbc implements StudentRepository {
    private static final String NAME_COLUMN = "name";
    private static final String SQL = String.format("SELECT NAME %S FROM STUDENT;", NAME_COLUMN);
    private static final String ADD_SQL = "INSERT INTO STUDENT (NAME) VALUES (?)";
    private static final String DELETE_BY_NAME_SQL = "DELETE FROM STUDENT WHERE NAME = ?";

    private final JdbcTemplate jdbcTemplate;
    private List<Student> students;

    public StudentRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        init();
    }

    private void init() {
        this.students = jdbcTemplate.query(SQL, (rs, numRow) -> new Student(rs.getString(NAME_COLUMN)));
    }

    @Override
    public boolean addStudent(String name) {
        int updateCount = jdbcTemplate.update(ADD_SQL, name);
        init();
        return updateCount > 0;
    }

    @Override
    public boolean removeStudent(String name) throws IllegalInitialDataException {
        int deleteCount = jdbcTemplate.update(DELETE_BY_NAME_SQL, name);
        init();
        return deleteCount > 0;
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
