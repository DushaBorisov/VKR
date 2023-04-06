package com.example.application.backend.repositories;

import com.example.application.backend.entities.enums.CreateAccountRequestStatus;
import com.example.application.backend.entities.models.CompanyRegisterRequestModel;
import com.example.application.backend.entities.models.StudentRegisterRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateAccountService {

    private final CompanyCreateAccountRequestRepository companyCreateAccountRequestRepository;
    private final StudentCreateAccountRequestRepository studentCreateAccountRequestRepository;


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

        studentCreateAccountRequestRepository.addNewStudentRegisterRequest(registerRequestModel);
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
        companyCreateAccountRequestRepository.addNewStudentRegisterRequest( registerRequestModel);
    }
}
