package com.example.application.backend.service;

import com.example.application.backend.entities.Company;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private List<Company> companyList = new ArrayList<>();

    @PostConstruct
    void initData() {
        companyList.add(new Company("company_user_name_1", 1L, "Company1", "IT company", "89654333566", "company1@gmail.com", List.of("IT")));
        companyList.add(new Company("company_user_name_2", 2L, "Company2", "Research company", "89654333566", "company1@gmail.com", List.of("Research")));
        companyList.add(new Company("company_user_name_3", 3L, "Company3", "Marketing company", "89654333566", "company1@gmail.com", List.of("Marketing")));
        companyList.add(new Company("company_user_name_4", 4L, "Company4", "Market place company", "89654333566", "company1@gmail.com", List.of("Market")));
        companyList.add(new Company("company_user_name_5", 5L, "Company5", "Technology company", "89654333566", "company1@gmail.com", List.of("Technology")));
        companyList.add(new Company("company_user_name_6", 6L, "Company6", "Products company", "89654333566", "company1@gmail.com", List.of("Products")));
        companyList.add(new Company("company_user_name_7", 7L, "Company7", "IT company", "89654333566", "company1@gmail.com", List.of("IT")));
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyList.stream().filter(el -> el.getCompanyId().equals(id)).findFirst();
    }

    public Optional<Company> getCompanyByUserName(String userName) {
        return companyList.stream().filter(el -> el.getCompanyUserName().equals(userName)).findFirst();
    }

    public List<Company> getCompaniesByLabels(List<String> labelsList) {
        return companyList.stream()
                .filter(labelsList::contains)
                .collect(Collectors.toList());
    }
}
