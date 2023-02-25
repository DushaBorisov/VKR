package com.example.application.ui;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "user", layout = MainLayout.class)
public class StudentPage extends VerticalLayout {

    private SecurityService securityService;
    Text info;


    public StudentPage(SecurityService securityService) {
        this.securityService = securityService;

        UserDetails userDetails = securityService.getAuthenticatedUser();
        String username = userDetails.getUsername();
        info = new Text("User: " + username);
        add(info);
    }
}
