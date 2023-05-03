package com.example.application.ui.student;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.CompanyResponseModel;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.CompanyResponseService;
import com.example.application.backend.service.StudentService;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
import com.example.application.ui.MainLayout;
import com.example.application.ui.company.JobInfoPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@PermitAll
@Route(value = "invitations", layout = MainLayout.class)
public class InvitationsListView extends Div implements AfterNavigationObserver {

    private Long studentId;
    Grid<CompanyResponseModel> grid = new Grid<>();

    private UserContext userContext;
    private StudentService studentService;
    private CompanyResponseService companyResponseService;


    @Autowired
    public InvitationsListView(CompanyResponseService companyResponseService, UserContext userContext, StudentService studentService) {
        this.companyResponseService = companyResponseService;
        this.userContext = userContext;
        this.studentService = studentService;

        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        String username = useData.get().getUserName();

        Optional<Student> studentOp = studentService.getStudentByUsername(username);
        studentId = studentOp.get().getStudentId();

        addClassName("card-list-view");
        setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(student -> createCard(student));
        add(grid);
    }

    private HorizontalLayout createCard(CompanyResponseModel companyResponseModel) {
        Company company = companyResponseModel.getJob().getCompany();
        Job job = companyResponseModel.getJob();

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
                .append("Компания : " + company.getName())
                .append("\n")
                .append("Заработная плата : " + job.getJobSalary() + "₽")
                .append("\n")
                .append("Занятость: " + job.getJobEmployment())
                .append("\n")
                .append("Опыт: " + job.getJobRequiredExperience());

        Span salary = new Span(bodyBuilder.toString());
        salary.addClassName("post");
        salary.getStyle().set("white-space", "pre-line");


        Button openButton = new Button("Смотреть вакансию");
        openButton.addClickListener(clickEvent ->
        {
            openButton.getUI().ifPresent(ui ->
                    ui.navigate(JobInfoPageInvitation.class, job.getJobId()));
        });

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.add(openButton);
        description.add(header, salary, buttonsContainer);
        card.add(description);

        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<CompanyResponseModel> companyResponseList = companyResponseService.getCompanyResponseModelByStudentId(studentId);
        grid.setItems(companyResponseList);
    }
}
