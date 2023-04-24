package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.CompanyResponseModel;
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
public class CompanyResponseRepository {

    @PersistenceContext
    private final EntityManager entityManager;


    public Optional<CompanyResponseModel> getCompanyResponseByJobIdAndStudentId(Long jobId, Long studentId) {
        CompanyResponseModel companyResponseModel;
        try {
            companyResponseModel = entityManager.createQuery(
                            "select cr from CompanyResponseModel cr " +
                                    "where cr.job.jobId = :jobId and cr.student.studentId = :studentId",
                            CompanyResponseModel.class)
                    .setParameter("jobId", jobId)
                    .setParameter("studentId", studentId)
                    .getSingleResult();

        } catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(companyResponseModel);
    }

    public List<CompanyResponseModel> getCompanyResponseByStudentId(Long studentId) {
        return entityManager.createQuery(
                        "select cr from CompanyResponseModel cr " +
                                "where cr.student.studentId = :studentId",
                        CompanyResponseModel.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    @Transactional
    public void addNewCompanyResponse(CompanyResponseModel companyResponseModel) {
        entityManager.persist(companyResponseModel);
    }

    @Transactional
    public void removeAllResponses() {
        entityManager.createQuery("delete from CompanyResponseModel").executeUpdate();
    }

}
