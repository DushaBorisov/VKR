package com.example.application.backend.service;

import com.example.application.backend.entities.models.Student;
import com.example.application.backend.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;


    public Optional<Student> getStudentByUsername(String userName) {
        return Optional.of(studentRepository.getStudentByUserName(userName));
    }

    public Optional<Student> getStudentById(Long studentId) {
        return Optional.of(studentRepository.getStudentById(studentId));
    }

    public void saveStudent(Student student) {
        studentRepository.addNewStudent(student);
    }

    public void updateStudent(Student student){
        studentRepository.updateStudent(student);
    }

    public void removeAll() {
        studentRepository.removeAllStudents();
    }

    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }
}
