package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.Student;
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
public class CompanyRepo {

    @PersistenceContext
    private final EntityManager entityManager;


    public Company getCompanyByUserName(String userMane) {
        return entityManager.createQuery("select c from Company c where c.user.username = :name", Company.class)
                .setParameter("name", userMane)
                .getSingleResult();
    }

    public Optional<Company> getCompanyById(Long companyId) {
       Company student;
        try {
            student = entityManager.createQuery("select c from Company c where c.companyId = :id", Company.class)
                    .setParameter("id", companyId)
                    .getSingleResult();
        }catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(student);
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

    @Transactional
    public void updateCompany(Company company){
        entityManager.merge(company);
    }

}
