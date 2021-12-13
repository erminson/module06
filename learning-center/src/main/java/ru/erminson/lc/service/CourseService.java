package ru.erminson.lc.service;

import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;

import java.util.List;

public interface CourseService {
    boolean add(Course course);
    boolean isExistCourseByTitle(String courseTitle);
    List<Course> findAll();
    Course findById(long id);
    Course getCourseByTitle(String title);
    List<Topic> getTopicsByCourseTitle(String title);
}
