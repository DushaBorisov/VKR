package com.example.application.backend.service;

import com.example.application.backend.elastic.JobElasticDocument;
import com.example.application.backend.elastic.JobSearchService;
import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.repositories.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    private final JobSearchService jobSearchService;
    private final JobRepository jobRepository;

    public List<Job> findByKeyWord(String keyWord) {
        List<Job> jobList = new ArrayList<>();

        try {
            List<JobElasticDocument> documentsList = jobSearchService.search(keyWord, 0, 100);
            documentsList.forEach(document -> {
                Optional<Job> jopOp = jobRepository.getJobById(document.getJobId());
                jopOp.ifPresent(jobList::add);
            });
        } catch (IOException e) {
            log.error("Unable to execute search job. Reason: {}", e.getCause(), e);
            return new ArrayList<>();
        }
        return jobList;
    }

    public List<Job> findByKeyWordsWithFilters(String keyWord, Set<EmploymentEnum> employmentEnum) {
        List<Job> jobs = findByKeyWord(keyWord);
        if (employmentEnum == null || employmentEnum.size() == 0) return jobs;
        Set<String> stringValues = employmentEnum.stream().map(EmploymentEnum::getEmploymentType).collect(Collectors.toSet());

        return jobs.stream().filter(job -> stringValues.contains(job.getJobEmployment())).collect(Collectors.toList());
    }

    public List<Job> getAllJobs() {
        return jobRepository.getAllJobs();
    }

    public void addNewJob(Job job) {
        // save job to db
        jobRepository.addNewJob(job);
        // save job to elastic
        JobElasticDocument jobDocument = JobElasticDocument.builder()
                .jobId(job.getJobId())
                .companyId(job.getCompany().getCompanyId())
                .jobTitle(job.getJobTitle())
                .jobDescription(job.getJobDescription())
                .jobStatus(job.getJobStatus())
                .build();
        try {
            jobSearchService.addSingleDocument(jobDocument);
        } catch (IOException e) {
            log.error("Unable to save new Job to elastic. Reason: {}", e.getMessage(), e);
        }
    }

    public void removeJob(Job job) {
        // remove job from elastic
        try {
            jobSearchService.removeDocument(job.getJobId());
        } catch (IOException e) {
            log.error("Unable to remove job document from elastic reason: {}", e.getMessage(), e);
            return;
        }
        // remove job from db
        jobRepository.removeJob(job);
    }

    public void removeAll() {
        jobRepository.removeAll();
    }
}
