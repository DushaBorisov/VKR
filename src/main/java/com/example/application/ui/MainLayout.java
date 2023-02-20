package com.example.application.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        createHeader();
        createDrawer();

    }

    private void createDrawer() {
        H1 logo = new H1("Students work");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createHeader() {
        RouterLink listView = new RouterLink("List", ListView.class);
        RouterLink more = new RouterLink("More", ElementView.class);
        RouterLink elements = new RouterLink("ListOfElements", ListOfElements.class);
        listView.setHighlightCondition(HighlightConditions.sameLocation());

        Scroller scroller = new Scroller(listView);
        addToDrawer(new VerticalLayout(
                listView,
                more,
                elements

        ));
    }
}

