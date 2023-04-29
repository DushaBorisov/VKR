package com.example.application.backend.service;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.repositories.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepository;

    public Optional<Company> getCompanyByUserName(String userName) {
        return Optional.of(companyRepository.getCompanyByUserName(userName));
    }

    public void saveCompany(Company company) {
        companyRepository.addNewCompany(company);
        // save company to elastic
    }

    public void getAllCompanies() {

    }

    public void removeAll() {
        companyRepository.removeAllCompanies();
    }

    public Optional<Company> getCompanyById(Long companyId) {
        return companyRepository.getCompanyById(companyId);
    }

    public void updateCompany(Company company){
        companyRepository.updateCompany(company);
    }

}
