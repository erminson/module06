package ru.erminson.lc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> courses() {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    public Course course(@PathVariable long id) {
        return courseService.findById(id);
    }
}
