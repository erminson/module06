package ru.erminson.lc.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.utils.RecordBookInitializer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RecordBookRepositoryJdbcTest {
    private final Student STUDENT1 = new Student(1, "Student1");
    private final Student STUDENT2 = new Student(2, "Student2");
    private final List<Topic> topics1 = new ArrayList<>() {{
        add(new Topic(1, "topic11", 1));
        add(new Topic(2, "topic12", 4));
        add(new Topic(3, "topic13", 8));
        add(new Topic(4, "topic14", 8));
        add(new Topic(5, "topic15", 16));
        add(new Topic(6, "topic16", 9));
    }};
    private final Course COURSE1 = new Course(1, "Course1", topics1);
    private final List<Topic> topics2 = new ArrayList<>() {{
        add(new Topic(7, "topic21", 1));
        add(new Topic(8, "topic22", 4));
        add(new Topic(9, "topic23", 8));
        add(new Topic(10, "topic24", 8));
        add(new Topic(11, "topic25", 16));
        add(new Topic(12, "topic26", 9));
    }};
    private final Course COURSE2 = new Course(2, "Course2", topics2);
    private RecordBookRepositoryJdbc recordBookRepositoryJdbc;
    private CourseRepositoryJdbc courseRepositoryJdbc;
    private StudentRepositoryJdbc studentRepositoryJdbc;

    @Autowired
    public RecordBookRepositoryJdbcTest(JdbcTemplate jdbcTemplate) {
        this.recordBookRepositoryJdbc = new RecordBookRepositoryJdbc(jdbcTemplate);
        this.courseRepositoryJdbc = new CourseRepositoryJdbc(jdbcTemplate);
        this.studentRepositoryJdbc = new StudentRepositoryJdbc(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        this.courseRepositoryJdbc.add(COURSE1);
        this.courseRepositoryJdbc.add(COURSE2);

        this.studentRepositoryJdbc.addStudent(STUDENT1.getName());
        this.studentRepositoryJdbc.addStudent(STUDENT2.getName());
    }

    @Test
    void shouldBeSameRecordBook() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(1, COURSE1);
        recordBookRepositoryJdbc.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        RecordBook actualRecordBook = recordBookRepositoryJdbc.getRecordBook(STUDENT1);
        assertThat(actualRecordBook, equalTo(expectedRecordBook1));
    }

    @Test
    void studentShouldBeOnCourse() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepositoryJdbc.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        assertThat(recordBookRepositoryJdbc.isStudentOnCourse(STUDENT1), is(true));
    }

    @Test
    void studentShouldNotBeOnCourse() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepositoryJdbc.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        assertThat(recordBookRepositoryJdbc.isStudentOnCourse(STUDENT2), is(false));
    }

    @Test
    void getAllStudentsTest() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepositoryJdbc.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        RecordBook expectedRecordBook2 = RecordBookInitializer.createRecordBookByCourse(COURSE2);
        recordBookRepositoryJdbc.addStudentWithRecordBook(STUDENT2, expectedRecordBook2);

        List<Student> students = recordBookRepositoryJdbc.getAllStudents();
        assertAll(
                () -> assertThat(students, hasSize(2)),
                () -> assertThat(students, contains(STUDENT1, STUDENT2))
        );
    }

    @Test
    void removeStudentFromCourseTest() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepositoryJdbc.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        RecordBook expectedRecordBook2 = RecordBookInitializer.createRecordBookByCourse(COURSE2);
        recordBookRepositoryJdbc.addStudentWithRecordBook(STUDENT2, expectedRecordBook2);

        recordBookRepositoryJdbc.removeStudentFromCourse(STUDENT1);

        List<Student> students = recordBookRepositoryJdbc.getAllStudents();
        assertAll(
                () -> assertThat(students, hasSize(1)),
                () -> assertThat(students, contains(STUDENT2))
        );
    }

    @AfterEach
    void tearDown() {
    }
}