package ru.erminson.lc.repository;

import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.IllegalInitialDataException;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    boolean add(Course course);
    boolean isExistCourseByTitle(String courseTitle);
    List<Course> getAllCourses();
    Optional<Course> findById(long id);
    Course getCourseByTitle(String title) throws IllegalInitialDataException;
    List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException;
}
