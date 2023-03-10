package com.example.application.backend.service;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.repositories.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepository;

    public Optional<Company> getCompanyByUserName(String userName) {
        return Optional.of(companyRepository.getCompanyByUserName(userName));
    }

    public void saveCompany(Company company){
        companyRepository.addNewCompany(company);
        // save company to elastic
    }

    public void getAllCompanies(){

    }

    public void removeAll(){
        companyRepository.removeAllCompanies();
    }

}
