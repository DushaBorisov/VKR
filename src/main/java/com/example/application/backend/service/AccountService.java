package com.example.application.backend.service;

import com.example.application.backend.elastic.CompanyRequestSearchService;
import com.example.application.backend.elastic.StudentRequestSearchService;
import com.example.application.backend.elastic.documents.CreateRequestCompany;
import com.example.application.backend.elastic.documents.CreateRequestStudent;
import com.example.application.backend.entities.enums.CreateAccountRequestStatus;
import com.example.application.backend.entities.models.CompanyRegisterRequestModel;
import com.example.application.backend.entities.models.StudentRegisterRequestModel;
import com.example.application.backend.repositories.CompanyCreateAccountRequestRepository;
import com.example.application.backend.repositories.StudentCreateAccountRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final CompanyCreateAccountRequestRepository companyCreateAccountRequestRepository;
    private final StudentCreateAccountRequestRepository studentCreateAccountRequestRepository;
    private final StudentRequestSearchService studentRequestSearchService;
    private CompanyRequestSearchService companyRequestSearchService;


    public void createStudentAccount(String name, String surname, String documentNumber,
                                     Integer courseOfStudy, String phoneNumber, String email) {
        StudentRegisterRequestModel registerRequestModel = StudentRegisterRequestModel.builder()
                .studentFirstName(name)
                .studentLastName(surname)
                .studentDocumentNumber(documentNumber)
                .studentCourseOfStudy(courseOfStudy)
                .studentPhoneNumber(phoneNumber)
                .studentEmail(email)
                .requestDate(LocalDateTime.now())
                .requestStatus(CreateAccountRequestStatus.REQUEST_CREATED)
                .build();

        // save to db
        studentCreateAccountRequestRepository.addNewStudentRegisterRequest(registerRequestModel);

        CreateRequestStudent createRequestStudent = CreateRequestStudent.builder()
                .requestId(registerRequestModel.getRequestId())
                .studentFirstName(registerRequestModel.getStudentFirstName())
                .studentLastName(registerRequestModel.getStudentLastName())
                .studentCourseOfStudy(registerRequestModel.getStudentCourseOfStudy())
                .studentEmail(registerRequestModel.getStudentEmail())
                .studentPhoneNumber(registerRequestModel.getStudentPhoneNumber())
                .studentDocumentNumber(registerRequestModel.getStudentDocumentNumber())
                .build();

        try {
            studentRequestSearchService.addSingleDocument(createRequestStudent);
        } catch (Exception ex) {
            log.error("Unable to save new CreateStudentRequest to elastic. Reason: {}", ex.getMessage(), ex);
        }
    }

    public List<StudentRegisterRequestModel> getAllStudentRequests() {
        return studentCreateAccountRequestRepository.getAllStudentRequests();
    }

    public List<CompanyRegisterRequestModel> getAllCompanyRequests() {
        return companyCreateAccountRequestRepository.getAllCompanyRequests();
    }


    public Optional<StudentRegisterRequestModel> getStudentRequestModelById(Long id) {
        return studentCreateAccountRequestRepository.getRequestByById(id);
    }

    public Optional<CompanyRegisterRequestModel> getCompanyRequestModelById(Long id) {
        return companyCreateAccountRequestRepository.getRequestByById(id);
    }

    public List<StudentRegisterRequestModel> findStudentRequestByKeyWordNameAndSurname(String keyWord) {
        List<StudentRegisterRequestModel> studentRequestList = new ArrayList<>();
        try {
            List<CreateRequestStudent> createRequestList = studentRequestSearchService.search(keyWord, 0, 100);
            createRequestList.forEach(document -> {
                Optional<StudentRegisterRequestModel> reqOp = studentCreateAccountRequestRepository.getRequestByById(document.getRequestId());
                reqOp.ifPresent(studentRequestList::add);
            });
        } catch (IOException e) {
            log.error("Unable to execute search job. Reason: {}", e.getCause(), e);
            return new ArrayList<>();
        }
        return studentRequestList;
    }

    public List<CompanyRegisterRequestModel> findCompanyRequestByKeyWordCompanyName(String keyWord) {
        List<CompanyRegisterRequestModel> companyRequestList = new ArrayList<>();
        try {
            List<CreateRequestCompany> createRequestList = companyRequestSearchService.search(keyWord, 0, 100);
            createRequestList.forEach(document -> {
                Optional<CompanyRegisterRequestModel> reqOp = companyCreateAccountRequestRepository.getRequestByById(document.getRequestId());
                reqOp.ifPresent(companyRequestList::add);
            });
        } catch (IOException e) {
            log.error("Unable to execute search job. Reason: {}", e.getCause(), e);
            return new ArrayList<>();
        }
        return companyRequestList;
    }

    public void createCompanyAccount(String name, String phone, String email, String description) {
        CompanyRegisterRequestModel registerRequestModel = CompanyRegisterRequestModel.builder()
                .companyName(name)
                .companyPhoneNumber(phone)
                .companyEmail(email)
                .companyDescription(description)
                .requestDate(LocalDateTime.now())
                .requestStatus(CreateAccountRequestStatus.REQUEST_CREATED)
                .build();
        companyCreateAccountRequestRepository.addNewStudentRegisterRequest(registerRequestModel);

        CreateRequestCompany createRequestCompany = CreateRequestCompany.builder()
                .requestId(registerRequestModel.getRequestId())
                .companyName(registerRequestModel.getCompanyName())
                .companyEmail(registerRequestModel.getCompanyEmail())
                .companyPhoneNumber(registerRequestModel.getCompanyPhoneNumber())
                .companyDescription(registerRequestModel.getCompanyDescription())
                .build();

        try {
            companyRequestSearchService.addSingleDocument(createRequestCompany);
        } catch (Exception ex) {
            log.error("Unable to save new CreateCompanyRequest to elastic. Reason: {}", ex.getMessage(), ex);
        }
    }
}
