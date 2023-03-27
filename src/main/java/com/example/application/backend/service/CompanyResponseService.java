package com.example.application.backend.service;

import com.example.application.backend.entities.models.CompanyResponseModel;
import com.example.application.backend.repositories.CompanyResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyResponseService {

    private final CompanyResponseRepository companyResponseRepository;
    private final StudentService studentService;
    private final CompanyService companyService;

    public Optional<CompanyResponseModel> getCompanyResponseModelByCompanyIdAndStudentId(Long companyId, Long studentId) {
        return companyResponseRepository.getCompanyResponseByJobIdAndStudentId(companyId, studentId);
    }

    public void saveCompanyResponseModel(CompanyResponseModel companyResponseModel) {
        if (companyResponseModel.getJob() == null)
            throw new RuntimeException("Job should not be null");
        if (companyResponseModel.getStudent() == null)
            throw new RuntimeException("Student should not be null");
        companyResponseRepository.addNewCompanyResponse(companyResponseModel);
    }

    public void deleteAllResponses(){
        companyResponseRepository.removeAllResponses();
    }
}
