package com.example.application.backend.service;

import com.example.application.backend.elastic.StudentSearchService;
import com.example.application.backend.elastic.documents.StudentElasticDocument;
import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Student;
import com.example.application.backend.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentSearchService studentSearchService;

    public List<Student> findByKeyWord(String keyWord) {
        List<Student> studentList = new ArrayList<>();
        if (keyWord != "") {
            try {
                List<StudentElasticDocument> documentsList = studentSearchService.search(keyWord, 0, 100);
                List<Student> finalStudentList = new ArrayList<>();
                documentsList.forEach(document -> {
                    Optional<Student> jopOp = studentRepository.getStudentById(document.getStudentId());
                    jopOp.ifPresent(finalStudentList::add);
                });
                studentList = finalStudentList;
            } catch (IOException e) {
                log.error("Unable to execute search job. Reason: {}", e.getCause(), e);
                return new ArrayList<>();
            }
        } else {
            studentList = getAllStudents();
        }

        return studentList;
    }

    public List<Student> findByKeyWordsWithFilters(String keyWord, Set<EmploymentEnum> employmentEnum) {
        List<Student> students = null;
        if (keyWord == "")
            students = getAllStudents();
        else
            students = findByKeyWord(keyWord);
        if (employmentEnum == null || employmentEnum.size() == 0) return students;
        Set<String> stringValues = employmentEnum.stream().map(EmploymentEnum::getEmploymentType).collect(Collectors.toSet());

        return students.stream()
                .filter(student -> stringValues.contains(student.getDesiredEmployment()))
                .filter(el -> el.getName() != null && el.getSurname() != null
                        && el.getCourseOfStudy() != null && el.getExperience() != null
                        && el.getResume() != null && el.getDesiredPosition() != null)
                .collect(Collectors.toList());
    }

    public void addNewStudent(Student student) {
        // save student to db
        studentRepository.addNewStudent(student);
        // save student to elastic
        StudentElasticDocument studentDocument = StudentElasticDocument.builder()
                .studentId(student.getStudentId())
                .desiredPosition(student.getDesiredPosition())
                .name(student.getName())
                .surname(student.getSurname())
                .resume(student.getResume())
                .build();
        try {
            studentSearchService.addSingleDocument(studentDocument);
        } catch (IOException e) {
            log.error("Unable to save new Student to elastic. Reason: {}", e.getMessage(), e);
        }
    }

    public void removeStudent(Student student) {
        // remove job from elastic
        try {
            studentSearchService.removeDocument(student.getStudentId());
        } catch (IOException e) {
            log.error("Unable to remove student document from elastic reason: {}", e.getMessage(), e);
            return;
        }
        // remove job from db
        studentRepository.removeStudent(student);
    }


    public Optional<Student> getStudentByUsername(String userName) {
        return Optional.of(studentRepository.getStudentByUserName(userName));
    }

    public Optional<Student> getStudentById(Long studentId) {
        return studentRepository.getStudentById(studentId);
    }

    public void saveStudent(Student student) {
        studentRepository.addNewStudent(student);
    }

    public void updateStudent(Student student) {
        studentRepository.updateStudent(student);
    }

    public void removeAll() {
        studentRepository.removeAllStudents();
    }

    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    public List<Student> getAllStudentsWithFiltrationNotEmptyData() {
        List<Student> studentList = studentRepository.getAllStudents();
        return studentList.stream().filter(
                el -> el.getName() != null && el.getSurname() != null
                        && el.getCourseOfStudy() != null && el.getExperience() != null
                        && el.getResume() != null && el.getDesiredPosition() != null
        ).collect(Collectors.toList());
    }
}
