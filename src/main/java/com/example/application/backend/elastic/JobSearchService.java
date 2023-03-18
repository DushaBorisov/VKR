package com.example.application.backend.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexTemplateRequest;
import com.example.application.backend.elastic.documents.JobElasticDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobSearchService {

    /**
     * Elasticsearch index name for Subject
     */
    private static final String SUBJECT_INDEX = "job";

    /**
     * Elasticsearch client instance
     */
    private final ElasticsearchClient client;


    /**
     * Search Jobs
     *
     * @param text text for search
     * @return search results as List
     * @throws IOException
     */
    public List<JobElasticDocument> search(String text, Integer offset, Integer limit) throws IOException {
        List<JobElasticDocument> searchResultList = new ArrayList<>();
        StringBuilder requestBuilder = new StringBuilder();

        // prepare search request. Use AND expression for terms
        String[] terms = text.split(" ");
        for (int i = 0; i < terms.length; i++) {
            requestBuilder.append(terms[i])
                    .append("*");
            if (i != terms.length - 1)
                requestBuilder.append(" + ");
        }

        String searchRequest = requestBuilder.toString();


        SearchResponse<JobElasticDocument> response = client.search(s -> s
                        .index(SUBJECT_INDEX)
                        .query(q -> q
                                .simpleQueryString(
                                        sqs -> sqs.query(searchRequest)
                                                .fields(List.of("job_title", "job_description"))
                                )
                        )
                        .from(offset)
                        .size(limit),
                JobElasticDocument.class
        );

        List<Hit<JobElasticDocument>> hits = response.hits().hits();
        for (Hit<JobElasticDocument> hit : hits) {
            JobElasticDocument job = hit.source();
            searchResultList.add(job);
        }
        return searchResultList;
    }

    /**
     * Add single document
     *
     * @param document document data
     * @throws IOException
     */
    public void addSingleDocument(JobElasticDocument document) throws IOException {
        IndexResponse response = client.index(i -> i
                .index(SUBJECT_INDEX)
                .id(String.valueOf(document.getJobId()))
                .document(document)
        );
        log.info(String.format("Index: %s. Id: %s. ", response.index(), response.id()));
    }

    /**
     * Removes element by jobId
     *
     * @param jobId job ID
     * @throws IOException
     */
    public void removeDocument(Long jobId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder()
                .index(SUBJECT_INDEX)
                .id(String.valueOf(jobId))
                .build();
        DeleteResponse response = client.delete(deleteRequest);
    }

    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest.Builder().index(SUBJECT_INDEX).build();
        DeleteIndexTemplateRequest rq = new DeleteIndexTemplateRequest.Builder().name(SUBJECT_INDEX).build();
        client.indices().deleteIndexTemplate(rq);
    }

    public void updateJobDocument(Long jobId, JobElasticDocument newDocument) throws IOException {
        Map<String, String> newValuesMap = new HashMap<>();

        if (newDocument.getJobTitle() != null) newValuesMap.put("job_title", newDocument.getJobTitle());
        if (newDocument.getJobDescription() != null)
            newValuesMap.put("job_description", newDocument.getJobDescription());
        if (newDocument.getJobEmployment() != null) newValuesMap.put("job_employment", newDocument.getJobEmployment());

        updateDocument(String.valueOf(jobId), newValuesMap);
    }

    private void updateDocument(String documentId, Map<String, String> valuesMap) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest.Builder<>().index(SUBJECT_INDEX)
                .id(documentId)
                .doc(valuesMap)
                .build();
        client.update(updateRequest, JobElasticDocument.class);
    }
}
