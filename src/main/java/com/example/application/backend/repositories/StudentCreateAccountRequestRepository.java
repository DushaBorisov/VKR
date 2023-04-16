package com.example.application.backend.repositories;

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
public class StudentCreateAccountRequestRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void addNewStudentRegisterRequest(StudentRegisterRequestModel studentResponseModel) {
        entityManager.persist(studentResponseModel);
    }

    @Transactional
    public void deleteCreateNewStudentRequest(Long requestId){
        StudentRegisterRequestModel request = entityManager.find(StudentRegisterRequestModel.class, requestId);
        entityManager.remove(request);
    }

    public List<StudentRegisterRequestModel> getAllStudentRequests() {
        return entityManager.createQuery("select r from StudentRegisterRequestModel r ", StudentRegisterRequestModel.class).getResultList();
    }

    public Optional<StudentRegisterRequestModel> getRequestByById(Long id) {
        StudentRegisterRequestModel req;
        try {
            req = entityManager.createQuery("select r from StudentRegisterRequestModel r where r.requestId = :id", StudentRegisterRequestModel.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(req);
    }

}
