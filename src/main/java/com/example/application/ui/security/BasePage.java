package com.example.application.ui.security;

import com.example.application.backend.entities.enums.AuthRoles;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
import com.example.application.ui.MainLayout;
import com.example.application.ui.admin.ListOfCreateStudentAccountRequestsView;
import com.example.application.ui.company.ListOfJobs;
import com.example.application.ui.student.ListOfStudents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "", layout = MainLayout.class)
public class BasePage extends Div implements BeforeEnterObserver {

    private UserContext userContext;

    public BasePage(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        // if role = USER
        if (useData.get().getRole().equals(AuthRoles.ROLE_USER.getRoleName())) {
            beforeEnterEvent.rerouteTo(ListOfJobs.class);
        }

        // if role = COMPANY
        if (useData.get().getRole().equals(AuthRoles.ROLE_COMPANY.getRoleName())) {
            beforeEnterEvent.rerouteTo(ListOfStudents.class);
        }

        // if role = ADMIN
        if (useData.get().getRole().equals(AuthRoles.ROLE_ADMIN.getRoleName())) {
            beforeEnterEvent.rerouteTo(ListOfCreateStudentAccountRequestsView.class);
        }
    }
}
