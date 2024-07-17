package com.madeeasy.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madeeasy.exception.DuplicateStudentException;
import com.madeeasy.model.Student;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FileRepository {
    private static final String FILE_NAME = "students.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Student> findAll() {
        try {
            createIfNotExists();
            Path filePath = getFilePath();
            if (Files.size(filePath) == 0) {
                return new ArrayList<>(); // Return empty list if file is empty
            }
            return objectMapper.readValue(filePath.toFile(), objectMapper.getTypeFactory().constructCollectionType(List.class, Student.class));
        } catch (IOException e) {
            throw new RuntimeException("Error finding all students", e);
        }
    }

    public Optional<Student> findById(Long id) {
        return findAll().stream().filter(student -> student.getId().equals(id)).findFirst();
    }

    public Student save(Student student) {
        List<Student> students = findAll();
        if (students.stream().anyMatch(existingStudent -> existingStudent.getEmail().equals(student.getEmail()) || existingStudent.getPhone().equals(student.getPhone()))) {
            throw new DuplicateStudentException("A student with the same email or phone number already exists.");
        }
        students.add(student);
        writeStudentsToFile(students);
        return student;
    }
    public Student update(Student student) {
        System.out.println("student = " + student);
        List<Student> students = findAll();
        for (Student existingStudent : students) {
            if (!existingStudent.getId().equals(student.getId())) {
                if (existingStudent.getEmail().equals(student.getEmail()) || existingStudent.getPhone().equals(student.getPhone())) {
                    throw new DuplicateStudentException("A student with the same email or phone number already exists.");
                }
            }
        }
        students.replaceAll(existingStudent -> existingStudent.getId().equals(student.getId()) ? student : existingStudent);
        writeStudentsToFile(students);
        return student;
    }

    public void delete(Student student) {
        List<Student> students = findAll();
        students.removeIf(existingStudent -> existingStudent.getId().equals(student.getId()));
        writeStudentsToFile(students);
    }

    private void writeStudentsToFile(List<Student> students) {
        try {
            Path filePath = getFilePath();
            objectMapper.writeValue(filePath.toFile(), students);
        } catch (IOException e) {
            throw new RuntimeException("Error writing students to file", e);
        }
    }

    private void createIfNotExists() {
        try {
            Path filePath = getFilePath();
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                objectMapper.writeValue(filePath.toFile(), new ArrayList<>()); // Write empty array initially
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating file: " + getFilePath(), e);
        }
    }

    private Path getFilePath() {
        return Paths.get("src/main/resources", FILE_NAME);
    }
}
