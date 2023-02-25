package com.example.application.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private String companyUserName;
    private Long companyId;
    private String companyName;
    private String description;
    private String companyContactPhone;
    private String companyContactEmail;
    private List<String> labels;

}
