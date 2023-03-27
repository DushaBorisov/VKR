package com.example.application.ui.company;

import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.service.CompanyService;
import com.example.application.backend.service.JobService;
import com.example.application.security.SecurityService;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@PermitAll
@RequiredArgsConstructor
@Route(value = "company-vacanciess-list", layout = MainLayout.class)
public class ListOfCompanyVacancies extends Div implements AfterNavigationObserver {

    private final Long companyId;

    private JobService jobService;
    private SecurityService securityService;
    private CompanyService companyService;

    Grid<Job> grid = new Grid<>();
    TextField filterText = new TextField();

    MultiSelectComboBox<EmploymentEnum> comboBox = new MultiSelectComboBox<>(
            "Занятость");

    @Autowired
    public ListOfCompanyVacancies(JobService jobService, SecurityService securService, CompanyService compService) {
        this.securityService = securService;
        this.jobService = jobService;
        this.companyService = compService;

        UserDetails userDetails = securityService.getAuthenticatedUser();
        String username = userDetails.getUsername();
        Optional<Company> companyOp = companyService.getCompanyByUserName(username);
        companyId = companyOp.get().getCompanyId();

        addClassName("card-list-view");
        setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(job -> createCard(job));
        add(grid);
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

        bodyBuilder
                .append("Заработная плата : " + job.getJobSalary() + "₽")
                .append("\n")
                .append("Занятость: " + job.getJobEmployment())
                .append("\n")
                .append("Требуемый опыт: " + job.getJobRequiredExperience());

        Span salary = new Span(bodyBuilder.toString());
        salary.addClassName("post");
        salary.getStyle().set("white-space", "pre-line");


        Button editButton = new Button("Редактировать");
        editButton.addClickListener(clickEvent ->
        {
//            editButton.getUI().ifPresent(ui ->
//                    ui.navigate(JobInfoPage.class, job.getJobId()));
        });

        Button responsesButton = new Button("Отклики на вакансию");
        responsesButton.addClickListener(clickEvent ->
        {
            responsesButton.getUI().ifPresent(ui ->
                    ui.navigate(ListOfStudentResponsesOnVacancy.class, job.getJobId()));
        });

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.add(editButton, responsesButton);

        description.add(header, salary, buttonsContainer);


        card.add(description);

        return card;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<Job> jobList = jobService.getAllCompanyJobsByCompanyId(companyId);
        grid.setItems(jobList);
    }

}
