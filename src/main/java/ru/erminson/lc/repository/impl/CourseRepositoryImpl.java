package ru.erminson.lc.repository.impl;

import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {
    private final List<Course> courses;

    public CourseRepositoryImpl() {
        courses = new ArrayList<>();
    }

    public CourseRepositoryImpl(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean add(Course course) {
        if (isExistCourseByTitle(course.getTitle())) {
            return false;
        }

        return courses.add(course);
    }

    @Override
    public boolean isExistCourseByTitle(String courseTitle) {
        return courses.stream()
                .map(Course::getTitle)
                .anyMatch(courseTitle::equals);
    }

    @Override
    public List<Course> getAllCourses() {
        return courses;
    }

    @Override
    public Course getCourseByTitle(String title) throws IllegalInitialDataException {
        if (courses.isEmpty()) {
            throw new IllegalInitialDataException("There are no courses");
        }
        return courses.stream()
                .filter(course -> course.getTitle().equals(title))
                .findFirst()
                .orElseThrow(IllegalInitialDataException::new);
    }

    @Override
    public List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException {
        Course course = this.getCourseByTitle(title);
        return course.getTopics();
    }
}
