package ru.erminson.lc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.erminson.lc.model.dto.request.StudentRequest;
import ru.erminson.lc.model.dto.response.StudentResponse;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.EntityHasAlreadyBeenCreated;
import ru.erminson.lc.model.exception.EntityNotFoundException;
import ru.erminson.lc.service.StudentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("student")
@Validated
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<StudentResponse> addStudent(
            @Valid @RequestBody StudentRequest studentRequest) throws EntityHasAlreadyBeenCreated {
        Student student = studentService.add(studentRequest);
        return ResponseEntity.ok(new StudentResponse(student.getId(), student.getName()));
    }

    @GetMapping
    public List<StudentResponse> getStudents() {
        return studentService.findAll().stream()
                .map(student -> new StudentResponse(student.getId(), student.getName()))
                .collect(Collectors.toList());
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
