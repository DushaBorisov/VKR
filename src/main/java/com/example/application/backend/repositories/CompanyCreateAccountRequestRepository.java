package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.CompanyRegisterRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyCreateAccountRequestRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void addNewStudentRegisterRequest(CompanyRegisterRequestModel companyRegisterRequestModel) {
        entityManager.persist(companyRegisterRequestModel);
    }
}
