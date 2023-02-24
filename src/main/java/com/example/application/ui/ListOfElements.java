package com.example.application.ui;

import com.example.application.backend.entities.Job;
import com.example.application.backend.service.JobService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
@Route(value = "", layout = MainLayout.class)
public class ListOfElements extends Div implements AfterNavigationObserver {

    private JobService jobService;

    Grid<Job> grid = new Grid<>();
    TextField filterText = new TextField();

    @Autowired
    public ListOfElements(JobService jobService) {

        this.jobService = jobService;

        addClassName("card-list-view");
        setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(job -> createCard(job));
        add(getToolbar(), grid);
    }

    private HorizontalLayout createCard(Job job) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(job.getTitle());
        name.addClassName("name");
        header.add(name);

        Span post = new Span(job.getDescription());
        post.addClassName("post");


        Button button = new Button("Подробнее");
        button.addClickListener(clickEvent ->
        {
            button.getUI().ifPresent(ui ->
                    ui.navigate(ElementView.class, job.getId()));
        });

        description.add(header, post, button);


        card.add(description);

        return card;
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Поск по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Поиск");


        HorizontalLayout toolBar = new HorizontalLayout(filterText, addContactButton);
        toolBar.addClassName("toolbar");
        return toolBar;
    }

    private void updateList() {
        grid.setItems(jobService.sortByName(filterText.getValue()));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<Job> jobList = jobService.getAllJobs();
        grid.setItems(jobList);
    }

}
