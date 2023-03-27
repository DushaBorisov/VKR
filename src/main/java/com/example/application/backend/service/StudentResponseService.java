package com.example.application.backend.service;

import com.example.application.backend.entities.models.StudentResponseModel;
import com.example.application.backend.repositories.StudentResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentResponseService {

    private final StudentResponseRepository studentResponseRepository;

    public Optional<StudentResponseModel> getStudentResponseModelByJobIdAndStudentId(Long jobId, Long studentId) {
        return studentResponseRepository.getStudentResponseByJobIdAndStudentId(jobId, studentId);
    }

    public void saveStudentResponseModel(StudentResponseModel studentResponseModel) {
        if (studentResponseModel.getJob() == null)
            throw new RuntimeException("Job should not be null");
        if (studentResponseModel.getStudent() == null)
            throw new RuntimeException("Student should not be null");
        studentResponseRepository.addNewStudentResponse(studentResponseModel);
    }

    public void deleteAllResponses() {
        studentResponseRepository.removeAllResponses();
    }

    public List<StudentResponseModel> getStudentResponsesByJobId(Long companyId) {
        return studentResponseRepository.getStudentResponsesByJobId(companyId);
    }
}
