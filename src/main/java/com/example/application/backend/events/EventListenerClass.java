package com.example.application.backend.events;

import com.example.application.backend.elastic.JobSearchService;
import com.example.application.backend.elastic.StudentSearchService;
import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.security.User;
import com.example.application.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventListenerClass {

    private final UserService userService;

    private final JobService jobService;

    private final CompanyService companyService;

    private final StudentService studentService;
    private final TestStudentsGenerator testStudentsGenerator;
    private final TestJobsGenerator testJobsGenerator;
    private final CompanyResponseService companyResponseService;
    private final StudentResponseService studentResponseService;

    private final JobSearchService jobSearchService;
    private final StudentSearchService studentSearchService;


    @EventListener(ApplicationReadyEvent.class)
    void afterStartUpLogic() {
        cleanDb();
        //cleanElastic();
        testJobsGenerator.generateTestCompaniesAndJobs();
        testStudentsGenerator.generateTestStudents();
        createTestAdmin();
    }

    private void cleanDb() {
        studentResponseService.deleteAllResponses();
        companyResponseService.deleteAllResponses();
        jobService.removeAll();
        companyService.removeAll();
        studentService.removeAll();
        userService.deleteAllUsers();
    }

//    private void cleanElastic(){
//        try {
//            jobSearchService.deleteIndex();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            studentSearchService.deleteIndex();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void createTestAdmin() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");
        userService.saveAdmin(user);
    }
}
