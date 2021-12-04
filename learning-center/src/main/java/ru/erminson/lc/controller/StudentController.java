package ru.erminson.lc.controller;

import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) throws EntityNotFoundException {
        studentService.deleteById(id);
    }
}
