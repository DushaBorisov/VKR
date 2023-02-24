package com.example.application.ui;

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

public class MainLayout extends AppLayout {

    private SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();

    }

    private void createDrawer() {
        RouterLink elements = new RouterLink("ListOfElements", ListOfElements.class);
        elements.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(new VerticalLayout(
                elements

        ));
    }

    private void createHeader() {
        H1 logo = new H1("Students work");
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

