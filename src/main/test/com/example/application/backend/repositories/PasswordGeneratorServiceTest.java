package com.example.application.backend.repositories;

import com.example.application.backend.service.PasswordGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordGeneratorServiceTest {

    @Autowired
    private PasswordGeneratorService passwordGeneratorService;

    @Test
    void generatePassword(){
        String password = passwordGeneratorService.generatePassword(10);
        System.out.println(password);
    }
}
