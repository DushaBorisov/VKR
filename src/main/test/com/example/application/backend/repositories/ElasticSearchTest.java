package com.example.application.backend.repositories;

import com.example.application.backend.elastic.documents.JobElasticDocument;
import com.example.application.backend.elastic.JobSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class ElasticSearchTest {

    @Autowired
    JobSearchService jobSearchService;

    @Test
    void testAddNewDocument() throws IOException {
        JobElasticDocument job = JobElasticDocument.builder()
                .jobId(1L)
                .companyId(1L)
                .jobTitle("Java разработчик")
                .jobDescription("Ищется java разработчик. Используемые технологии: Spring boot, PostgreSQL")
                .jobStatus("Свободна")
                .build();

        jobSearchService.addSingleDocument(job);
    }

    @Test
    void testSearchJob() throws IOException {
        List<JobElasticDocument> jobList = jobSearchService.search("Java", 0, 100);
        System.out.println("gg");

    }

    @Test
    void testDeleteIndex() throws IOException {
        jobSearchService.deleteIndex();
    }

    @Test
    void shouldUpdateDocument() throws IOException {
        JobElasticDocument newJob = JobElasticDocument.builder()
                .jobTitle("Стажер разработчик на языке Java")
                .jobEmployment("Стажировка")
                .jobDescription("Новое описание вакансии")
                .build();
        jobSearchService.updateJobDocument(2L, newJob);
    }
}
