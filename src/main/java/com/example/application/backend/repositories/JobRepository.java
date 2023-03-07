package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public Job getJobById(Long id) {
        return entityManager.createQuery("select j from Job j where j.jobId = :id", Job.class)
                .setParameter("id", id)
                .getSingleResult();
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
}
