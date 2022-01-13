package ru.erminson.lc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.erminson.lc.model.dto.request.StudentRequest;
import ru.erminson.lc.model.dto.request.TopicScoreRequest;
import ru.erminson.lc.model.dto.response.StudentResponse;
import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.model.exception.EntityHasAlreadyBeenCreated;
import ru.erminson.lc.model.exception.EntityNotFoundException;
import ru.erminson.lc.service.RecordBookService;
import ru.erminson.lc.service.StudentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("students")
@Validated
public class StudentController {

    private final StudentService studentService;
    private final RecordBookService recordBookService;

    public StudentController(StudentService studentService, RecordBookService recordBookService) {
        this.studentService = studentService;
        this.recordBookService = recordBookService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(
            @Valid @RequestBody StudentRequest studentRequest) throws EntityHasAlreadyBeenCreated {
        Student student = studentService.add(studentRequest);

        return ResponseEntity.ok(new StudentResponse(student.getId(), student.getName()));
    }

    @GetMapping
    public List<StudentResponse> getStudents(UsernamePasswordAuthenticationToken token) {
        return studentService.findAll().stream()
                .map(student -> new StudentResponse(student.getId(), student.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable long id) throws EntityNotFoundException {
        return studentService.findById(id);
    }

    @GetMapping("/{id}/recordbook")
    public RecordBook getStudentRecordBook(@PathVariable long id) throws EntityNotFoundException {
        return recordBookService.getRecordBookByStudent(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable long id) throws EntityNotFoundException {
        studentService.deleteById(id);

        return ResponseEntity.ok("Student with id: " + id + " deleted with success");
    }

    @PutMapping("/{id}/topic/{topicId}")
    public ResponseEntity<String> rate(
            @PathVariable long id,
            @PathVariable long topicId,
            @RequestBody TopicScoreRequest topicScoreRequest) {
        recordBookService.rateTopic(topicId, topicScoreRequest.getScore());

        return ResponseEntity.ok("TopicScore updated");
    }

    @PostMapping("/{id}/course/{courseId}")
    public ResponseEntity<String> enrollOnCourse(@PathVariable long id, @PathVariable long courseId) {
        recordBookService.enrollStudentOnCourse(id, courseId);

        return ResponseEntity.ok("Student enrolled");
    }
}
