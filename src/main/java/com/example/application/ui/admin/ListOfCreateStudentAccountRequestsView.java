package com.example.application.ui.admin;

import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.models.StudentRegisterRequestModel;
import com.example.application.backend.entities.models.StudentResponseModel;
import com.example.application.backend.service.AccountService;
import com.example.application.ui.MainLayout;
import com.example.application.ui.company.JobInfoPage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PermitAll
@Route(value = "request-student-list", layout = MainLayout.class)
public class ListOfCreateStudentAccountRequestsView extends Div implements AfterNavigationObserver {

    private AccountService accountService;

    Grid<StudentRegisterRequestModel> grid = new Grid<>();
    TextField filterText = new TextField();



    @Autowired
    public ListOfCreateStudentAccountRequestsView(AccountService accountService) {
        this.accountService = accountService;

        addClassName("card-list-view");
        setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(req-> createCard(req));
        add(getToolbar(), grid);
    }

    private HorizontalLayout createCard(StudentRegisterRequestModel registerRequestModel) {
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

        Span name = new Span(registerRequestModel.getStudentFirstName() + " " + registerRequestModel.getStudentLastName());
        name.addClassName("name");
        header.add(name);

        StringBuilder bodyBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        bodyBuilder.append("Курс обучения : " + registerRequestModel.getStudentCourseOfStudy())
                .append("\n")
                .append("Номер студенческого билета : " + registerRequestModel.getStudentDocumentNumber())
                .append("\n")
                .append("Дата заявки: " + formatter.format(registerRequestModel.getRequestDate()));

        Span salary = new Span(bodyBuilder.toString());
        salary.addClassName("post");
        salary.getStyle().set("white-space", "pre-line");


        Button button = new Button("Подробнее");
        button.addClickListener(clickEvent ->
        {
            button.getUI().ifPresent(ui ->
                    ui.navigate(StudentRequestInfo.class, registerRequestModel.getRequestId()));
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


        HorizontalLayout toolBar = new HorizontalLayout(filterText, searchButton);
        toolBar.setAlignItems(FlexComponent.Alignment.END);
        toolBar.setPadding(true);

        toolBar.addClassName("toolbar");
        return toolBar;
    }

    private void updateList() {
        grid.setItems(accountService.findStudentRequestByKeyWordNameAndSurname(filterText.getValue()));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<StudentRegisterRequestModel> jobList = accountService.getAllStudentRequests();
        grid.setItems(jobList);
    }
}
