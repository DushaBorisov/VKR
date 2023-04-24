package com.example.application.ui;

import com.example.application.backend.entities.enums.AuthRoles;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
import com.example.application.ui.admin.ListOfCreateCompanyAccountRequestsView;
import com.example.application.ui.admin.ListOfCreateStudentAccountRequestsView;
import com.example.application.ui.company.CompanyPage;
import com.example.application.ui.company.CreateJobView;
import com.example.application.ui.company.ListOfCompanyVacancies;
import com.example.application.ui.company.ListOfJobs;
import com.example.application.ui.security.CustomLoginView;
import com.example.application.ui.student.InvitationsListView;
import com.example.application.ui.student.ListOfStudents;
import com.example.application.ui.student.StudentPage;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private UserContext userContext;

    @Autowired
    public MainLayout(UserContext userContext) {
        this.userContext = userContext;
        createHeader();
        createDrawer();

    }

    private void createDrawer() {
        RouterLink jobList = new RouterLink("Список вакансий", ListOfJobs.class);
        RouterLink studentsList = new RouterLink("Список студентов", ListOfStudents.class);
        RouterLink userPage = new RouterLink("Страница студента", StudentPage.class);
        RouterLink companyPage = new RouterLink("Страница компании", CompanyPage.class);
        RouterLink listOfCompanyVacancies = new RouterLink("Вакансии компании", ListOfCompanyVacancies.class);
        RouterLink listOfStudentRequests = new RouterLink("Заявки студентов", ListOfCreateStudentAccountRequestsView.class);
        RouterLink listOfCompanyRequests = new RouterLink("Заявки компаний", ListOfCreateCompanyAccountRequestsView.class);
        RouterLink invitationList = new RouterLink("Приглашения", InvitationsListView.class);
        RouterLink createJob = new RouterLink("Создание вакансии", CreateJobView.class);

        VerticalLayout listOfPages = new VerticalLayout();

        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        if (useData.isPresent()) {

            VaadinSession.getCurrent().getSession().getId();
            // if role = USER
            if (useData.get().getRole().equals(AuthRoles.ROLE_USER.getRoleName())) {
                listOfPages.add(userPage, jobList, invitationList);
            }
            // if role = COMPANY
            if (useData.get().getRole().equals(AuthRoles.ROLE_COMPANY.getRoleName())) {
                listOfPages.add(companyPage, studentsList, listOfCompanyVacancies, createJob);
            }
            // if role = ADMIN
            if (useData.get().getRole().equals(AuthRoles.ROLE_ADMIN.getRoleName())) {
                listOfPages.add(listOfStudentRequests, listOfCompanyRequests);
            }
        }

        jobList.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(listOfPages);

    }

    private void createHeader() {
        H1 logo = new H1("Вакансии МГТУ");
        logo.addClassNames("text-l", "m-m");

        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Button logoutButton = new Button("Выход", e -> {
            userContext.deleteUserFromContext(sessionId);
            getUI().get().navigate(CustomLoginView.class);
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutButton);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.expand(logo);
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        if (useData.isEmpty()) beforeEnterEvent.rerouteTo(CustomLoginView.class);
    }
}

