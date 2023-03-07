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

    private List<Company> companyList = new ArrayList<>();
//
//    @PostConstruct
//    void initData() {
//        companyList.add(new Company("test_company", 1L, "ООО Айтико", "Компания ITco (Айтико) специализируется в области IT-решений в сфере здравоохранения.\n" +
//                "\n" +
//                "Мы обладаем уникальным целостным подходом, что позволяет разрабатывать удачные технологии для больниц и полностью интегрированные решения в масштабе всего лечебного учреждения. Эти специализированные решения предназначены для интеграции IT-систем и средств обработки данных в радиологии, кардиологии, маммографии и ортопедии.\n" +
//                "\n" +
//                "Нашими клиентами являются крупнейшие частные и государственные медицинские учреждения на территории России, ряда стран СНГ и Юго-Восточной Европы.\n" +
//                "\n" +
//                "Компания является авторизованным бизнес-партнером компании Agfa HealthCare (Бельгия) - одного из лидирующих на мировом рынке производителей программного обеспечения в сфере решений PACS систем и решений для обработки медицинских диагностических изображений.", "89654333566", "company1@gmail.com", List.of("IT")));
//        companyList.add(new Company("company_user_name_2", 2L, "Company2", "Research company", "89654333566", "company1@gmail.com", List.of("Research")));
//        companyList.add(new Company("company_user_name_3", 3L, "Company3", "Marketing company", "89654333566", "company1@gmail.com", List.of("Marketing")));
//        companyList.add(new Company("company_user_name_4", 4L, "Company4", "Market place company", "89654333566", "company1@gmail.com", List.of("Market")));
//        companyList.add(new Company("company_user_name_5", 5L, "Company5", "Technology company", "89654333566", "company1@gmail.com", List.of("Technology")));
//        companyList.add(new Company("company_user_name_6", 6L, "Company6", "Products company", "89654333566", "company1@gmail.com", List.of("Products")));
//        companyList.add(new Company("company_user_name_7", 7L, "Company7", "IT company", "89654333566", "company1@gmail.com", List.of("IT")));
//    }


    public Optional<Company> getCompanyByUserName(String userName) {
        return Optional.of(companyRepository.getCompanyByUserName(userName));
    }

    public void saveCompany(Company company){
        companyRepository.addNewCompany(company);
        // save company to elastic
    }

    public void getAllCompanies(){

    }

}
