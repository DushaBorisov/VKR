package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.CompanyRegisterRequestModel;
import com.example.application.backend.entities.models.StudentRegisterRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyCreateAccountRequestRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void addNewStudentRegisterRequest(CompanyRegisterRequestModel companyRegisterRequestModel) {
        entityManager.persist(companyRegisterRequestModel);
    }

    @Transactional
    public void deleteCreateNewCompanyRequest(Long requestId) {
        CompanyRegisterRequestModel request = entityManager.find(CompanyRegisterRequestModel.class, requestId);
        entityManager.remove(request);
    }

    public List<CompanyRegisterRequestModel> getAllCompanyRequests() {
        return entityManager.createQuery("select r from CompanyRegisterRequestModel r ", CompanyRegisterRequestModel.class).getResultList();
    }

    public Optional<CompanyRegisterRequestModel> getRequestByById(Long id) {
        CompanyRegisterRequestModel req;
        try {
            req = entityManager.createQuery("select r from CompanyRegisterRequestModel r where r.requestId = :id", CompanyRegisterRequestModel.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(req);
    }
}
