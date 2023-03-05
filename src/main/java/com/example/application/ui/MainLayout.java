package com.example.application.ui;

import com.example.application.backend.entities.enums.AuthRoles;
import com.example.application.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class MainLayout extends AppLayout {

    private SecurityService securityService;

    @Autowired
    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();

    }

    private void createDrawer() {
        RouterLink elements = new RouterLink("Список вакансий", ListOfElements.class);
        RouterLink userPage = new RouterLink("Страница студента", StudentPage.class);
        RouterLink companyPage = new RouterLink("Страница компании", CompanyPage.class);

        VerticalLayout listOfPages = new VerticalLayout();

        listOfPages.add(elements);

        UserDetails userDetails = securityService.getAuthenticatedUser();
        Collection<GrantedAuthority> list = (Collection<GrantedAuthority>) userDetails.getAuthorities();
        // if role = USER
        if(list.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(AuthRoles.ROLE_USER.getRoleName())){
            listOfPages.add(userPage);
        }
        // if role = COMPANY
        if(list.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(AuthRoles.ROLE_COMPANY.getRoleName())){
            listOfPages.add(companyPage);
        }
        // if role = ADMIN
        if(list.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(AuthRoles.ROLE_ADMIN.getRoleName())){
        }

        elements.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(listOfPages);
    }

    private void createHeader() {
        H1 logo = new H1("Вакансии МГТУ");
        logo.addClassNames("text-l", "m-m");

        Button logoutButton = new Button("Log out", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutButton);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.expand(logo);
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }
}

