package com.example.application.backend.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
