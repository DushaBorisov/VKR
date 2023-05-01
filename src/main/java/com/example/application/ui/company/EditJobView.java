package com.example.application.ui.company;

import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.service.CompanyService;
import com.example.application.backend.service.JobService;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;

import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.Optional;

@PermitAll
@Route(value = "job/edit", layout = MainLayout.class)
public class EditJobView extends VerticalLayout implements HasUrlParameter<Long> {

    private UserContext userContext;
    private JobService jobService;
    private CompanyService companyService;
    private Long jobId;

    private Label error;
    private Long companyId;

    private H2 title;
    private TextField jobTitle;
    private Select<String> jobEmployment;
    private TextField jobSalary;
    private TextField jobExperience;

    private Button moveBackButton;
    private TextArea description;

    private Button editJobButton;

    public EditJobView(JobService jobSer, UserContext userCont, CompanyService companyService) {
        this.userContext = userCont;
        this.companyService = companyService;
        this.jobService = jobSer;

        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        String username = useData.get().getUserName();

        Optional<Company> companyOp = companyService.getCompanyByUserName(username);
        companyId = companyOp.get().getCompanyId();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.jobId = id;
        Optional<Job> jopOp = jobService.getJobById(id);
        if (jopOp.isEmpty()) {
            showError();
            return;
        }
        drawEditPage(jopOp.get());
    }


    private void showError() {
        error = new Label("Error");
        add(error);
    }

    private void drawEditPage(Job job) {
        VerticalLayout container = new VerticalLayout();

        title = new H2("Редактирование вакансии");
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        Span info = new Span("Для изменения данных вакансии заполните все поля соответсвующей информацией");
        info.addClassNames(LumoUtility.TextColor.SECONDARY);

        jobTitle = new TextField("Название вакансии");
        jobTitle.setValue(job.getJobTitle());

        jobEmployment = new Select<>();
        jobEmployment.setLabel("Тип занятости");
        jobEmployment.setItems(Arrays.stream(EmploymentEnum.values()).map(en -> en.getEmploymentType()));

        jobExperience = new TextField("Требуемый опыт");
        jobExperience.setValue(job.getJobRequiredExperience());

        jobSalary = new TextField("Заработная плата");
        jobSalary.setValue(job.getJobSalary().toString());

        description = new TextArea();
        description.setWidthFull();
        description.setLabel("Описание вакансии");
        description.setValue(job.getJobDescription());

        FormLayout formLayout = new FormLayout();
        formLayout.add(jobTitle, jobEmployment, jobExperience, jobSalary);

        HorizontalLayout buttonsContainer = new HorizontalLayout();

        editJobButton = new Button("Обновить вакансию", e -> updateJob(job));
        moveBackButton = new Button("Назад");
        moveBackButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        buttonsContainer.add(editJobButton, moveBackButton);

        container.add(title, info, formLayout, description, buttonsContainer);
        add(container);

    }

    private void updateJob(Job job) {
        if (!checkCorrectData()) {
            Notification.show("Необходимо заполнить все поля!");
            return;
        }

        job.setJobTitle(jobTitle.getValue());
        job.setJobEmployment(jobEmployment.getValue());
        job.setJobRequiredExperience(jobExperience.getValue());
        job.setJobSalary(Integer.valueOf(jobSalary.getValue()));
        job.setJobDescription(description.getValue());

        jobService.updateJob(job);
        Notification.show("Вакансия обновлена!");
    }

    private boolean checkCorrectData() {
        if (jobTitle.isEmpty()) return false;
        if (jobEmployment.isEmpty()) return false;
        if (jobExperience.isEmpty()) return false;
        if (jobSalary.isEmpty()) return false;
        if (description.isEmpty()) return false;
        return true;
    }
}
