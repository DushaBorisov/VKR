package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.StudentRegisterRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class StudentCreateAccountRequestRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void addNewStudentRegisterRequest(StudentRegisterRequestModel studentResponseModel) {
        entityManager.persist(studentResponseModel);
    }

}
