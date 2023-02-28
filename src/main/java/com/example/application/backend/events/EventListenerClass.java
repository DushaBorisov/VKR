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
    }

    private void createTestUser() {
        User user = new User();
        user.setUsername("andrey_user");
        user.setPassword("password");
        userService.saveUser(user);
    }
}
