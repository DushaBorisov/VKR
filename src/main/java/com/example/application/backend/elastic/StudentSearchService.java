package com.example.application.backend.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexTemplateRequest;
import com.example.application.backend.elastic.documents.JobElasticDocument;
import com.example.application.backend.elastic.documents.StudentElasticDocument;
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
public class StudentSearchService {

    /**
     * Elasticsearch index name for Subject
     */
    private static final String STUDENT_INDEX = "student";

    /**
     * Elasticsearch client instance
     */
    private final ElasticsearchClient client;

    /**
     * Search Student
     *
     * @param text text for search
     * @return search results as List
     * @throws IOException
     */
    public List<StudentElasticDocument> search(String text, Integer offset, Integer limit) throws IOException {
        List<StudentElasticDocument> searchResultList = new ArrayList<>();
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
        SearchResponse<StudentElasticDocument> response = client.search(s -> s
                        .index(STUDENT_INDEX)
                        .query(q -> q
                                .simpleQueryString(
                                        sqs -> sqs.query(searchRequest)
                                                .fields(List.of("desired_position"))
                                )
                        )
                        .from(offset)
                        .size(limit),
                StudentElasticDocument.class
        );

        List<Hit<StudentElasticDocument>> hits = response.hits().hits();
        for (Hit<StudentElasticDocument> hit : hits) {
            StudentElasticDocument job = hit.source();
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
    public void addSingleDocument(StudentElasticDocument document) throws IOException {
        IndexResponse response = client.index(i -> i
                .index(STUDENT_INDEX)
                .id(String.valueOf(document.getStudentId()))
                .document(document)
        );
        log.info(String.format("Index: %s. Id: %s. ", response.index(), response.id()));
    }

    /**
     * Removes element by jobId
     *
     * @param studentId job ID
     * @throws IOException
     */
    public void removeDocument(Long studentId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder()
                .index(STUDENT_INDEX)
                .id(String.valueOf(studentId))
                .build();
        DeleteResponse response = client.delete(deleteRequest);
    }

    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest.Builder().index(STUDENT_INDEX).build();
        DeleteIndexTemplateRequest rq = new DeleteIndexTemplateRequest.Builder().name(STUDENT_INDEX).build();
        client.indices().deleteIndexTemplate(rq);
    }

    public void updateStudentDocument(Long studentId, StudentElasticDocument newDocument) throws IOException {
        Map<String, String> newValuesMap = new HashMap<>();

        if (newDocument.getName() != null) newValuesMap.put("name", newDocument.getName());
        if (newDocument.getSurname() != null) newValuesMap.put("surname", newDocument.getSurname());
        if (newDocument.getDesiredPosition() != null)
            newValuesMap.put("desired_position", newDocument.getDesiredPosition());
        if (newDocument.getResume() != null) newValuesMap.put("resume", newDocument.getResume());

        updateDocument(String.valueOf(studentId), newValuesMap);
    }

    private void updateDocument(String documentId, Map<String, String> valuesMap) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest.Builder<>().index(STUDENT_INDEX)
                .id(documentId)
                .doc(valuesMap)
                .build();
        client.update(updateRequest, StudentElasticDocument.class);
    }
}
