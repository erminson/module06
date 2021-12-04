package ru.erminson.lc.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.IllegalInitialDataException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class StudentRepositoryJdbcTest {
    private final StudentRepositoryJdbc studentRepositoryJdbc;

    private final Student STUDENT1 = new Student(1,"Student1");
    private final Student STUDENT2 = new Student(2,"Student2");
    private final Student STUDENT3 = new Student(3,"Student3");

    @Autowired
    public StudentRepositoryJdbcTest(JdbcTemplate jdbcTemplate) {
        studentRepositoryJdbc = new StudentRepositoryJdbc(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldBeTreeStudentsTest() {
        studentRepositoryJdbc.save(STUDENT1.getName());
        studentRepositoryJdbc.save(STUDENT2.getName());
        studentRepositoryJdbc.save(STUDENT3.getName());
        assertEquals(3, studentRepositoryJdbc.findAll().size());
    }

    @Test
    void shouldBeFalseWhenAddStudentTwice() {
        assertAll(
                () -> assertThat(studentRepositoryJdbc.save(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepositoryJdbc.save(STUDENT1.getName()), is(false))
        );
    }

    @Test
    void shouldBeTrueWhenRemoveOneStudent() {
        studentRepositoryJdbc.save(STUDENT1.getName());
        studentRepositoryJdbc.save(STUDENT2.getName());
        studentRepositoryJdbc.save(STUDENT3.getName());

        assertAll(
                () -> assertThat(studentRepositoryJdbc.deleteByName(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepositoryJdbc.findAll(), hasSize(2))
        );
    }

    @Test
    void shouldBeExceptionWhenRemoveNotExistsStudent() {
        assertThrows(IllegalInitialDataException.class, () -> studentRepositoryJdbc.deleteByName(STUDENT1.getName()));
    }

    @Test
    void shouldBeZeroStudentsWhenRemoveAll() {
        studentRepositoryJdbc.save(STUDENT1.getName());
        studentRepositoryJdbc.save(STUDENT2.getName());
        studentRepositoryJdbc.save(STUDENT3.getName());

        assertAll(
                () -> assertThat(studentRepositoryJdbc.deleteByName(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepositoryJdbc.deleteByName(STUDENT2.getName()), is(true)),
                () -> assertThat(studentRepositoryJdbc.deleteByName(STUDENT3.getName()), is(true)),
                () -> assertThat(studentRepositoryJdbc.findAll(), hasSize(0))
        );
    }

    @Test
    void shouldThrowExceptionWhenFindNotExistStudents() {
        assertThrows(IllegalInitialDataException.class, () -> studentRepositoryJdbc.getStudentByName("dummy name"));
    }

    @Test
    void isExistStudentTestMethod() {
        studentRepositoryJdbc.save(STUDENT1.getName());

        assertAll(
                () -> assertThat(studentRepositoryJdbc.isExistsStudent(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepositoryJdbc.isExistsStudent("dummy name"), is(false))
        );
    }

    @AfterEach
    void tearDown() {
    }
}