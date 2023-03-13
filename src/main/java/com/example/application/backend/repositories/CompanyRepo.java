package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyRepo {

    @PersistenceContext
    private final EntityManager entityManager;


    public Company getCompanyByUserName(String userMane) {
        return entityManager.createQuery("select c from Company c where c.user.username = :name", Company.class)
                .setParameter("name", userMane)
                .getSingleResult();
    }

    public List<Company> getAllCompanies() {
        return entityManager.createQuery("select c from Company c ", Company.class).getResultList();
    }

    @Transactional
    public void addNewCompany(Company company) {
        entityManager.persist(company);
    }

    @Transactional
    public void removeAllCompanies(){
        entityManager.createQuery("delete from Company").executeUpdate();
    }

}
