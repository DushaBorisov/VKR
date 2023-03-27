package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.StudentResponseModel;
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
public class StudentResponseRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public Optional<StudentResponseModel> getStudentResponseByJobIdAndStudentId(Long jobId, Long studentId) {
        StudentResponseModel companyResponseModel;
        try {
            companyResponseModel = entityManager.createQuery(
                            "select sr from StudentResponseModel sr " +
                                    "where sr.job.jobId = :jobId and sr.student.studentId = :studentId",
                            StudentResponseModel.class)
                    .setParameter("jobId", jobId)
                    .setParameter("studentId", studentId)
                    .getSingleResult();

        } catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(companyResponseModel);
    }

    public List<StudentResponseModel> getStudentResponsesByJobId(Long jobId) {
        return entityManager.createQuery("select sr from StudentResponseModel sr where sr.job.jobId = :id ", StudentResponseModel.class)
                .setParameter("id", jobId)
                .getResultList();
    }

    @Transactional
    public void addNewStudentResponse(StudentResponseModel studentResponseModel) {
        entityManager.persist(studentResponseModel);
    }

    @Transactional
    public void removeAllResponses() {
        entityManager.createQuery("delete from StudentResponseModel").executeUpdate();
    }
}
