package ru.erminson.lc.service.impl;

import org.springframework.stereotype.Service;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.model.entity.Topic;
import ru.erminson.lc.model.exception.EntityNotFoundException;
import ru.erminson.lc.model.exception.IllegalInitialDataException;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.service.CourseService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

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
    public List<Course> findAll() {
        return courseRepository.getAllCourses();
    }

    @Override
    public Course findById(long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.orElseThrow(() -> new EntityNotFoundException(Course.class, "id", String.valueOf(id)));
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
