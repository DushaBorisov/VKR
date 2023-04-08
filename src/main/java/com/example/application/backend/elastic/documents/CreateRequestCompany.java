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
public class CreateRequestCompany {

    @JsonProperty("request_id")
    private Long requestId;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("company_phone_number")
    private String companyPhoneNumber;

    @JsonProperty("company_email")
    private String companyEmail;

    @JsonProperty("company_description")
    private String companyDescription;
}
