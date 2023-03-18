package com.example.application.ui.company;

import com.example.application.backend.entities.models.Job;
import com.example.application.backend.service.JobService;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "user", layout = MainLayout.class)
public class JobInfoPage extends VerticalLayout implements HasUrlParameter<Long> {

    private Long jobId;

    private JobService jobService;

    private Label error;
    private H2 title;
    private Span salary;
    private Span requiredExperience;
    private Span requredEmployment;
    private Span descriptionTitle;
    private Paragraph description;

    private Accordion contacts;
    private Button responseButton;


    @Autowired
    public JobInfoPage(JobService jobSer) {
        this.jobService = jobSer;
        addClassName("person-form-view");

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.jobId = id;
        createView(jobId);
    }

    public void createView(Long jobId) {
        Optional<Job> jobOp = jobService.getJobById(jobId);
        if (jobOp.isEmpty()) showError();
        Job job = jobOp.get();

        VerticalLayout container = new VerticalLayout();

        title = new H2(job.getJobTitle());
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        salary = new Span("Заработная плата: " + job.getJobSalary() + "₽");
        salary.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        requiredExperience = new Span("Требуемый опыт: " + job.getJobRequiredExperience());
        requiredExperience.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        requredEmployment = new Span("Занятость: " + job.getJobEmployment());
        requredEmployment.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        descriptionTitle = new Span("Описание вакансии: ");
        descriptionTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.BLACK);

        description = new Paragraph(job.getJobDescription());
        description.getStyle().set("white-space", "pre-line");
        description.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);


        contacts = new Accordion();
        Span email = new Span(job.getCompany().getEmail());
        Span phone = new Span(job.getCompany().getPhoneNumber());
        VerticalLayout personalInformationLayout = new VerticalLayout(email, phone);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);
        contacts.add("Контактная информация", personalInformationLayout);

        responseButton = new Button("Откликнуться");

        container.add(title, salary,requiredExperience, requredEmployment, descriptionTitle, description, contacts, responseButton);
        add(container);
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }


}
