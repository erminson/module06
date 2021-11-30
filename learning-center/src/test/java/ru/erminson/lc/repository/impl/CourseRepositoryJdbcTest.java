package ru.erminson.lc.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.IllegalInitialDataException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class CourseRepositoryJdbcTest {
    private final CourseRepositoryJdbc courseRepositoryJdbc;

    private final List<Topic> topics1 = new ArrayList<>() {{
        add(new Topic(1, "topic11", 1));
        add(new Topic(2, "topic12", 4));
        add(new Topic(3, "topic13", 8));
        add(new Topic(4, "topic14", 8));
        add(new Topic(5, "topic15", 16));
        add(new Topic(6, "topic16", 9));
    }};
    private final Course course1 = new Course(1, "Course1", topics1);

    private final List<Topic> topics2 = new ArrayList<>() {{
        add(new Topic(7, "topic21", 1));
        add(new Topic(8, "topic22", 4));
        add(new Topic(9, "topic23", 8));
        add(new Topic(10, "topic24", 8));
        add(new Topic(11, "topic25", 16));
        add(new Topic(12, "topic26", 9));
    }};
    private final Course course2 = new Course(2, "Course2", topics2);

    @Autowired
    public CourseRepositoryJdbcTest(JdbcTemplate jdbcTemplate) {
        courseRepositoryJdbc = new CourseRepositoryJdbc(jdbcTemplate);
    }

    @Test
    void whenAddTwoDifferentCoursesShouldBeTwo() {
        courseRepositoryJdbc.add(course1);
        courseRepositoryJdbc.add(course2);

        List<Course> courses = courseRepositoryJdbc.getAllCourses();
        assertEquals(2, courses.size());
    }

    @Test
    void whenAddCourseTwoTimesMustBeOneCourse() {
        courseRepositoryJdbc.add(course1);
        courseRepositoryJdbc.add(course1);

        List<Course> courses = courseRepositoryJdbc.getAllCourses();
        assertThat(courses, hasSize(1));
    }

    @Test
    void shouldBeEqualsTitle() throws IllegalInitialDataException {
        courseRepositoryJdbc.add(course1);

        String expectedCourseTitle = course1.getTitle();
        Course course = courseRepositoryJdbc.getCourseByTitle(expectedCourseTitle);
        assertEquals(expectedCourseTitle, course.getTitle());
    }

    @Test
    void shouldThrowExceptionIfWrongCourseTitle() {
        assertThrows(
                IllegalInitialDataException.class,
                () -> courseRepositoryJdbc.getCourseByTitle("Wrong course title")
        );
    }

    @Test
    void shouldGetCorrectListOfTopics() throws IllegalInitialDataException {
        courseRepositoryJdbc.add(course1);

        String expectedTitle = course1.getTitle();
        int expectedCountTopics = course1.getTopics().size();

        List<Topic> actualTopics = courseRepositoryJdbc.getTopicsByCourseTitle(expectedTitle);
        assertAll(
                () -> assertThat(actualTopics, hasSize(expectedCountTopics)),
                () -> assertThat(actualTopics, hasItems(topics1.get(0), topics1.get(1), topics1.get(2))),
                () -> assertEquals(expectedCountTopics, actualTopics.size())
        );
    }
    
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}