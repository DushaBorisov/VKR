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
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.Optional;

@PermitAll
@Route(value = "create-job", layout = MainLayout.class)
public class CreateJobView extends VerticalLayout {



    private Long companyId;
    private UserContext userContext;
    private CompanyService companyService;
    private JobService jobService;

    private H2 title;
    private TextField jobTitle;
    private Select<String> jobEmployment;
    private TextField jobSalary;
    private TextField jobExperience;

    private TextArea description;

    private Button creteJobButton;


    @Autowired
    public CreateJobView(UserContext userCont, CompanyService companyService, JobService jobServ) {
        this.userContext = userCont;
        this.companyService = companyService;
        this.jobService = jobServ;

        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        String username = useData.get().getUserName();

        Optional<Company> companyOp = companyService.getCompanyByUserName(username);
        companyId = companyOp.get().getCompanyId();
        drawCreateJobPage();

    }


    private void drawCreateJobPage() {
        VerticalLayout container = new VerticalLayout();

        title = new H2("Создание новой вакансии");
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        Span info = new Span("Для создания новой вакансии заполните все поля соответсвующей информацией");
        info.addClassNames(LumoUtility.TextColor.SECONDARY);

        jobTitle = new TextField("Название вакансии");
        jobEmployment = new Select<>();
        jobEmployment.setLabel("Тип занятости");
        jobEmployment.setItems(Arrays.stream(EmploymentEnum.values()).map(en -> en.getEmploymentType()));

        jobExperience = new TextField("Требуемый опыт");

        jobSalary = new TextField("Заработная плата");

        description = new TextArea();
        description.setWidthFull();
        description.setLabel("Описание вакансии");

        FormLayout formLayout = new FormLayout();
        formLayout.add(jobTitle, jobEmployment, jobExperience, jobSalary);

        creteJobButton = new Button("Создать вакансию", e -> saveNewJob());

        container.add(title, info, formLayout, description, creteJobButton);
        add(container);
    }

    private boolean checkCorrectData() {
        if (jobTitle.isEmpty()) return false;
        if (jobEmployment.isEmpty()) return false;
        if (jobExperience.isEmpty()) return false;
        if (jobSalary.isEmpty()) return false;
        if (description.isEmpty()) return false;
        return true;
    }

    private void clearAllFields(){
        jobTitle.clear();
        jobEmployment.clear();
        jobExperience.clear();
        jobSalary.clear();
        description.clear();
    }

    private void saveNewJob(){
        if(!checkCorrectData()) {
            Notification.show("Необходимо заполнить все поля!");
            return;
        }

        Optional<Company> compOp =  companyService.getCompanyById(companyId);

        Job job = Job.builder()
                .company(compOp.get())
                .jobTitle(jobTitle.getValue())
                .jobDescription(description.getValue())
                .jobSalary(Integer.valueOf(jobSalary.getValue()))
                .jobEmployment(jobEmployment.getValue())
                .jobRequiredExperience(jobExperience.getValue())
                .build();

        jobService.addNewJob(job);

        Notification.show(String.format("Новая вакансия: '%s' добавлена!", jobTitle.getValue()));
        clearAllFields();
    }
}
