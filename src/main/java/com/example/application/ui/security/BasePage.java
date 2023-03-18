package com.example.application.ui.security;

import com.example.application.backend.entities.enums.AuthRoles;
import com.example.application.security.SecurityService;
import com.example.application.ui.MainLayout;
import com.example.application.ui.company.ListOfJobs;
import com.example.application.ui.student.ListOfStudents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;
import java.util.Collection;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "", layout = MainLayout.class)
public class BasePage extends Div implements BeforeEnterObserver {

    private SecurityService securityService;

    public BasePage(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        UserDetails userDetails = this.securityService.getAuthenticatedUser();
        Collection<GrantedAuthority> list = (Collection<GrantedAuthority>) userDetails.getAuthorities();
        // if role = USER
        if (list.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(AuthRoles.ROLE_USER.getRoleName())) {
            beforeEnterEvent.rerouteTo(ListOfJobs.class);
        }

        // if role = COMPANY
        if (list.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(AuthRoles.ROLE_COMPANY.getRoleName())) {
            beforeEnterEvent.rerouteTo(ListOfStudents.class);
        }
    }
}
