package com.example.application.ui.company;

import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.service.JobService;
import com.example.application.ui.ElementView;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Set;

@PermitAll
@RequiredArgsConstructor
@Route(value = "job-info", layout = MainLayout.class)
public class ListOfJobs extends Div implements AfterNavigationObserver {

    private JobService jobService;

    Grid<Job> grid = new Grid<>();
    TextField filterText = new TextField();

    MultiSelectComboBox<EmploymentEnum> comboBox = new MultiSelectComboBox<>(
            "Занятость");

    @Autowired
    public ListOfJobs(JobService jobService) {
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

        Span name = new Span(job.getJobTitle());
        name.addClassName("name");
        header.add(name);

        StringBuilder bodyBuilder = new StringBuilder();

        bodyBuilder.append("Компания : " + job.getCompany().getName())
                .append("\n")
                .append("Заработная плата : " + job.getJobSalary() + "₽")
                .append("\n")
                .append("Занятость: " + job.getJobEmployment())
                .append("\n")
                .append("Требуемый опыт: " + job.getJobRequiredExperience());

        Span salary = new Span(bodyBuilder.toString());
        salary.addClassName("post");
        salary.getStyle().set("white-space", "pre-line");


        Button button = new Button("Подробнее");
        button.addClickListener(clickEvent ->
        {
            button.getUI().ifPresent(ui ->
                    ui.navigate(JobInfoPage.class, job.getJobId()));
        });

        description.add(header, salary, button);


        card.add(description);

        return card;
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Поск по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setLabel("Ключевое слово");

        //filterText.addValueChangeListener(e -> updateList());

        Button searchButton = new Button("Поиск");
        searchButton.addClickListener(
                clickEvent->{
                    updateList();
                }
        );


        comboBox.setItems(EmploymentEnum.values());
        comboBox.setItemLabelGenerator(EmploymentEnum::getEmploymentType);


        HorizontalLayout toolBar = new HorizontalLayout(filterText, comboBox, searchButton);
        toolBar.setAlignItems(FlexComponent.Alignment.END);
        toolBar.setPadding(true);

        toolBar.addClassName("toolbar");
        return toolBar;
    }

    private void updateList() {
        grid.setItems(jobService.findByKeyWordsWithFilters(filterText.getValue(), comboBox.getSelectedItems()));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<Job> jobList = jobService.getAllJobs();
        grid.setItems(jobList);
    }

}
