package com.example.application.backend.repositories;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.security.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CompanyRepoTest {

    @Autowired
    CompanyRepo companyRepo;

    @Autowired
    UserRepository userRepository;

    @Test
    void test() {
        Company company = companyRepo.getCompanyByUserName("test_company");
        System.out.println(company.getName());
    }

    @Test
    void selectAllCompaniesTest() {
        List<Company> companiesList = companyRepo.getAllCompanies();
        System.out.println("Companies list size: " + companiesList.size());
    }

    @Test
    void addNewCompanyTest() {
        User user = userRepository.findByUsername("test_company");
        Company company = Company.builder()
                .user(user)
                .name("Test_company")
                .description("description")
                .phoneNumber("999999")
                .email("email")
                .build();

        companyRepo.addNewCompany(company);
    }

    @Test
    void deleteAllCompaniesTest(){
        companyRepo.removeAllCompanies();
    }
}