package com.example.application.backend.repositories;

import com.example.application.backend.service.EmailNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailTest {

    @Autowired
    private EmailNotificationService emailNotificationService;


    @Test
    void sendEmail(){
        emailNotificationService.sendSimpleEmail("borisovandrey.work@gmail.com", "Тестовое сообщение", "Текст тестового сообщения");
    }
}
