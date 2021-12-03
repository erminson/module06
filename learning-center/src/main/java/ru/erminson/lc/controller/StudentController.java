package ru.erminson.lc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erminson.lc.model.exception.EntityNotFoundException;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable long id) throws EntityNotFoundException {
        return studentService.findById(id);
    }
}
