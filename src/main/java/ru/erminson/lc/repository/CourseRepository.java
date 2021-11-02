package ru.erminson.lc.repository;

import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.IllegalInitialDataException;

import java.util.List;

public interface CourseRepository {
    boolean add(Course course);
    boolean isExistCourseByTitle(String courseTitle);
    List<Course> getAllCourses();
    Course getCourseByTitle(String title) throws IllegalInitialDataException;
    List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException;
}
