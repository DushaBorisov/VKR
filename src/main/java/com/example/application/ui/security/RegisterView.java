package com.example.application.ui.security;

import com.example.application.backend.entities.enums.AuthRoles;
import com.example.application.backend.entities.security.User;
import com.example.application.backend.service.UserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.security.PermitAll;

@PermitAll
@Route("register")
public class RegisterView extends VerticalLayout {

    private final UserService userService;

    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final Button registerButton = new Button("Register", this::register);

    public RegisterView(UserService userService) {
        this.userService = userService;

        add(usernameField, passwordField, registerButton);
        setAlignItems(Alignment.CENTER);
        setWidth("100%");
    }

    private void register(ClickEvent<Button> event) {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            Notification.show("Please enter a username and password");
            return;
        }

        try {
            userService.loadUserByUsername(username);
            Notification.show("Username already taken");
            return;
        }catch (UsernameNotFoundException ex){
            User user = new User(username, password);
            userService.saveUser(user, AuthRoles.ROLE_USER);

            Notification.show("User registered successfully");
            UI.getCurrent().navigate(LoginView.class);
        }

        }
    }


