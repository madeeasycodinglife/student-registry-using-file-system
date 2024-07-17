package com.madeeasy.service;

import com.madeeasy.dto.request.StudentRequestDTO;
import com.madeeasy.dto.response.StudentResponseDTO;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<?> createStudent(StudentRequestDTO studentRequest);

    ResponseEntity<?> getAllStudents();

    ResponseEntity<?> updateStudent(StudentRequestDTO studentRequest, Long id);

    ResponseEntity<?> deleteStudent(Long id);

    ResponseEntity<?> partialUpdate(StudentRequestDTO studentUpdateRequest, Long id);

    ResponseEntity<?> getStudentById(Long id);
}
