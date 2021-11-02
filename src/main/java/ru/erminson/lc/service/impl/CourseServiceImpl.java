package ru.erminson.lc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.service.CourseService;

import java.util.Collections;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean add(Course course) {
        return courseRepository.add(course);
    }

    @Override
    public boolean isExistCourseByTitle(String courseTitle) {
        return courseRepository.isExistCourseByTitle(courseTitle);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses();
    }

    @Override
    public Course getCourseByTitle(String title) {
        try {
            return courseRepository.getCourseByTitle(title);
        } catch (IllegalInitialDataException e) {
            return null;
        }
    }

    @Override
    public List<Topic> getTopicsByCourseTitle(String title) {
        try {
            return courseRepository.getTopicsByCourseTitle(title);
        } catch (IllegalInitialDataException e) {
            return Collections.emptyList();
        }
    }
}
