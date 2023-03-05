package com.example.application.backend.events;

import com.example.application.backend.entities.security.User;
import com.example.application.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventListenerClass {

    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    void afterStartUpLogic() {
        createTestUser();
        createTestCompany();
        createTestAdmin();
    }

    private void createTestUser() {
        User user = new User();
        user.setUsername("andrey_user");
        user.setPassword("password");
        userService.saveStudent(user);
    }

    private void createTestCompany() {
        User user = new User();
        user.setUsername("test_company");
        user.setPassword("password");
        userService.saveCompany(user);
    }

    private void createTestAdmin() {
        User user = new User();
        user.setUsername("test_admin");
        user.setPassword("password");
        userService.saveAdmin(user);
    }
}
