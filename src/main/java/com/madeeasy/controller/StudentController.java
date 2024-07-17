package com.madeeasy.controller;

import com.madeeasy.dto.request.StudentRequestDTO;
import com.madeeasy.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/student-service")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping(path = "/get-all-students")
    public ResponseEntity<?> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping(path = "/get-student/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createStudent(@RequestBody StudentRequestDTO studentRequest) {
        return studentService.createStudent(studentRequest);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody StudentRequestDTO studentRequest,
                                           @PathVariable("id") Long id) {
        return studentService.updateStudent(studentRequest, id);
    }

    @PatchMapping(path = "/partial-update/{id}")
    public ResponseEntity<?> partialUpdate(@RequestBody StudentRequestDTO studentUpdateRequest,
                                           @PathVariable("id") Long id) {
        return studentService.partialUpdate(studentUpdateRequest, id);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id) {
        return studentService.deleteStudent(id);
    }
}
