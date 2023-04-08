package com.example.application.backend.elastic.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRequestStudent {

    @JsonProperty("request_id")
    private Long requestId;

    @JsonProperty("student_first_name")
    private String studentFirstName;

    @JsonProperty("student_last_name")
    private String studentLastName;

    @JsonProperty("student_course_of_study")
    private Integer studentCourseOfStudy;

    @JsonProperty("student_document_number")
    private String studentDocumentNumber;

    @JsonProperty("student_phone_number")
    private String studentPhoneNumber;

    @JsonProperty("student_email")
    private String studentEmail;

}
