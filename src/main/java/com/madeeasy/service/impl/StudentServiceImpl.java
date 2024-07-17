package com.madeeasy.service.impl;


import com.madeeasy.dto.request.StudentRequestDTO;
import com.madeeasy.dto.response.StudentResponseDTO;
import com.madeeasy.exception.DuplicateStudentException;
import com.madeeasy.exception.StudentNotFoundException;
import com.madeeasy.model.Student;
import com.madeeasy.repository.FileRepository;
import com.madeeasy.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final FileRepository fileRepository;
    private final AtomicLong counter = new AtomicLong();

    @Override
    public ResponseEntity<?> createStudent(StudentRequestDTO studentRequest) {
        StudentResponseDTO studentResponse = null;
        try {
            Student student = Student.builder()
                    .id(counter.incrementAndGet())
                    .firstName(studentRequest.getFirstName())
                    .lastName(studentRequest.getLastName())
                    .email(studentRequest.getEmail())
                    .phone(studentRequest.getPhone())
                    .build();
            Student savedStudent = this.fileRepository.save(student);
            studentResponse = StudentResponseDTO.builder()
                    .id(savedStudent.getId())
                    .firstName(savedStudent.getFirstName())
                    .lastName(savedStudent.getLastName())
                    .email(savedStudent.getEmail())
                    .phone(savedStudent.getPhone())
                    .build();
        } catch (DuplicateStudentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.ok().body(studentResponse);
    }

    @Override
    public ResponseEntity<?> getAllStudents() {
        List<Student> studentList = this.fileRepository.findAll();
        if (studentList.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(studentList.stream().map(student -> StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .build()).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> updateStudent(StudentRequestDTO studentRequest, Long id) {
        Student student = this.fileRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found for id " + id));
        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setEmail(studentRequest.getEmail());
        student.setPhone(studentRequest.getPhone());
        try {
            Student updatedStudent = this.fileRepository.update(student);
            StudentResponseDTO studentResponse = StudentResponseDTO.builder()
                    .id(updatedStudent.getId())
                    .firstName(updatedStudent.getFirstName())
                    .lastName(updatedStudent.getLastName())
                    .email(updatedStudent.getEmail())
                    .phone(updatedStudent.getPhone())
                    .build();
            return ResponseEntity.ok().body(studentResponse);
        } catch (DuplicateStudentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteStudent(Long id) {
        Student student = this.fileRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found for id " + id));
        this.fileRepository.delete(student);
        return ResponseEntity.ok().body("Student deleted successfully whose id " + id);
    }

    @Override
    public ResponseEntity<?> partialUpdate(StudentRequestDTO studentUpdateRequest, Long id) {
        Student student = this.fileRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found for id " + id));
        if (studentUpdateRequest.getFirstName() != null && !studentUpdateRequest.getFirstName().isBlank())
            student.setFirstName(studentUpdateRequest.getFirstName());
        if (studentUpdateRequest.getLastName() != null && !studentUpdateRequest.getLastName().isBlank())
            student.setLastName(studentUpdateRequest.getLastName());
        if (studentUpdateRequest.getEmail() != null && !studentUpdateRequest.getEmail().isBlank())
            student.setEmail(studentUpdateRequest.getEmail());
        if (studentUpdateRequest.getPhone() != null && !studentUpdateRequest.getPhone().isBlank())
            student.setPhone(studentUpdateRequest.getPhone());
        try {
            Student updatedStudent = this.fileRepository.update(student);
            StudentResponseDTO studentResponse = StudentResponseDTO.builder()
                    .id(updatedStudent.getId())
                    .firstName(updatedStudent.getFirstName())
                    .lastName(updatedStudent.getLastName())
                    .email(updatedStudent.getEmail())
                    .phone(updatedStudent.getPhone())
                    .build();
            return ResponseEntity.ok().body(studentResponse);
        } catch (DuplicateStudentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getStudentById(Long id) {
        Student student = this.fileRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found for id " + id));
        StudentResponseDTO studentResponse = StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .build();
        return ResponseEntity.ok().body(studentResponse);
    }
}
