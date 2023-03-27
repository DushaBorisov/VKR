package com.example.application.ui.company;

import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.models.Student;
import com.example.application.backend.entities.models.StudentResponseModel;
import com.example.application.backend.service.CompanyService;
import com.example.application.backend.service.JobService;
import com.example.application.backend.service.StudentResponseService;
import com.example.application.security.SecurityService;
import com.example.application.ui.MainLayout;
import com.example.application.ui.student.StudentInfoPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@PermitAll
@RequiredArgsConstructor
@Route(value = "company-vacancies-responses", layout = MainLayout.class)
public class ListOfStudentResponsesOnVacancy extends Div implements HasUrlParameter<Long> {
    private final Long companyId;
    private Long jobId;

    private JobService jobService;
    private SecurityService securityService;
    private CompanyService companyService;

    private StudentResponseService studentResponseService;

    Grid<StudentResponseModel> grid = new Grid<>();

    @Autowired
    public ListOfStudentResponsesOnVacancy(JobService jobService, SecurityService securService,
                                           CompanyService compService, StudentResponseService studentRespSer) {
        this.securityService = securService;
        this.jobService = jobService;
        this.companyService = compService;
        this.studentResponseService = studentRespSer;

        UserDetails userDetails = securityService.getAuthenticatedUser();
        String username = userDetails.getUsername();
        Optional<Company> companyOp = companyService.getCompanyByUserName(username);
        companyId = companyOp.get().getCompanyId();

        addClassName("card-list-view");
        setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(stResp -> createCard(stResp));
        add(grid);
    }

    private HorizontalLayout createCard(StudentResponseModel studentResponse) {
        Student student = studentResponse.getStudent();

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

        Span name = new Span(student.getDesiredPosition());
        name.addClassName("name");
        header.add(name);

        StringBuilder bodyBuilder = new StringBuilder();

        bodyBuilder
                .append("Заработная плата : " + student.getDesiredSalary() + "₽")
                .append("\n")
                .append("Занятость: " + student.getDesiredEmployment())
                .append("\n")
                .append("Опыт: " + student.getExperience());

        Span salary = new Span(bodyBuilder.toString());
        salary.addClassName("post");
        salary.getStyle().set("white-space", "pre-line");


        Button openButton = new Button("Смотреть резюме");
        openButton.addClickListener(clickEvent ->
        {
            openButton.getUI().ifPresent(ui ->
                    ui.navigate(StudentInfoPage.class, student.getStudentId()));
        });

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.add(openButton);
        description.add(header, salary, buttonsContainer);
        card.add(description);

        return card;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.jobId = id;
        List<StudentResponseModel> studentResponsesList = studentResponseService.getStudentResponsesByJobId(jobId);
        grid.setItems(studentResponsesList);
    }

}
