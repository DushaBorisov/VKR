package com.example.application.backend.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexTemplateRequest;
import com.example.application.backend.elastic.documents.CreateRequestCompany;
import com.example.application.backend.elastic.documents.CreateRequestStudent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyRequestSearchService {

    /**
     * Elasticsearch index name for Subject
     */
    private static final String SUBJECT_INDEX = "request_company";

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
    public List<CreateRequestCompany> search(String text, Integer offset, Integer limit) throws IOException {
        List<CreateRequestCompany> searchResultList = new ArrayList<>();
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


        SearchResponse<CreateRequestCompany> response = client.search(s -> s
                        .index(SUBJECT_INDEX)
                        .query(q -> q
                                .simpleQueryString(
                                        sqs -> sqs.query(searchRequest)
                                                .fields(List.of("company_name"))
                                )
                        )
                        .from(offset)
                        .size(limit),
                CreateRequestCompany.class
        );

        List<Hit<CreateRequestCompany>> hits = response.hits().hits();
        for (Hit<CreateRequestCompany> hit : hits) {
            CreateRequestCompany req = hit.source();
            searchResultList.add(req);
        }
        return searchResultList;
    }

    /**
     * Add single document
     *
     * @param document document data
     * @throws IOException
     */
    public void addSingleDocument(CreateRequestCompany document) throws IOException {
        IndexResponse response = client.index(i -> i
                .index(SUBJECT_INDEX)
                .id(String.valueOf(document.getRequestId()))
                .document(document)
        );
        log.info(String.format("Index: %s. Id: %s. ", response.index(), response.id()));
    }

    /**
     * Removes element by jobId
     *
     * @param reqId request ID
     * @throws IOException
     */
    public void removeDocument(Long reqId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder()
                .index(SUBJECT_INDEX)
                .id(String.valueOf(reqId))
                .build();
        DeleteResponse response = client.delete(deleteRequest);
    }

    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest.Builder().index(SUBJECT_INDEX).build();
        DeleteIndexTemplateRequest rq = new DeleteIndexTemplateRequest.Builder().name(SUBJECT_INDEX).build();
        client.indices().deleteIndexTemplate(rq);
    }
}
