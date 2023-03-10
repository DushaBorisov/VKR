package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.Job;
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
public class JobRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public Optional<Job> getJobById(Long id) {
        Job job;
        try {
            job = entityManager.createQuery("select j from Job j where j.jobId = :id", Job.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.ofNullable(job);
    }

    public List<Job> getAllJobs() {
        return entityManager.createQuery("select j from Job j ", Job.class)
                .getResultList();
    }

    @Transactional
    public void addNewJob(Job job) {
        entityManager.persist(job);
    }

    @Transactional
    public void removeJob(Job job) {
        entityManager.remove(job);
    }

    @Transactional
    public void removeAll(){
        entityManager.createQuery("delete  from Job ").executeUpdate();
    }
}
