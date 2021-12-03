package ru.erminson.lc.repository.impl;

import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.repository.AbstractCourseRepository;

import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryImpl extends AbstractCourseRepository {
    public CourseRepositoryImpl() {
        courses = new ArrayList<>();
    }

    public CourseRepositoryImpl(List<Course> courses) {
        this.courses = courses;
    }
}
