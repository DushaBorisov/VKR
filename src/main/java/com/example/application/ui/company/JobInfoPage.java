package com.example.application.ui.company;

import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.models.Student;
import com.example.application.backend.entities.models.StudentResponseModel;
import com.example.application.backend.service.CompanyService;
import com.example.application.backend.service.JobService;
import com.example.application.backend.service.StudentResponseService;
import com.example.application.backend.service.StudentService;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.util.Optional;

@PermitAll
@Route(value = "user", layout = MainLayout.class)
public class JobInfoPage extends VerticalLayout implements HasUrlParameter<Long> {

    private Long jobId;
    private Long studentId;
    private Long companyId;

    private JobService jobService;
    private StudentResponseService studentResponseService;
    private UserContext userContext;
    private StudentService studentService;
    private CompanyService companyService;


    private Label error;
    private H2 title;
    private Span salary;
    private Span requiredExperience;
    private Span requiredEmployment;
    private Span descriptionTitle;
    private Paragraph description;

    private Accordion contacts;

    private Button responseButton;
    private Label responseAlreadySend;
    private Label companySendInvitation;


    @Autowired
    public JobInfoPage(JobService jobSer, StudentResponseService studentRespService,
                       UserContext userContext, StudentService stService, CompanyService compService) {
        this.jobService = jobSer;
        this.companyService = compService;
        this.studentResponseService = studentRespService;
        this.userContext = userContext;
        this.studentService = stService;
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

        // set Company id
        companyId = job.getCompany().getCompanyId();

        String sessionId = VaadinSession.getCurrent().getSession().getId();
        // set Student id
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        String username = useData.get().getUserName();
        Optional<Student> studentOp = studentService.getStudentByUsername(username);
        studentId = studentOp.get().getStudentId();

        VerticalLayout container = new VerticalLayout();

        title = new H2(job.getJobTitle());
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        salary = new Span("Заработная плата: " + job.getJobSalary() + "₽");
        salary.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        requiredExperience = new Span("Требуемый опыт: " + job.getJobRequiredExperience());
        requiredExperience.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        requiredEmployment = new Span("Занятость: " + job.getJobEmployment());
        requiredEmployment.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

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

        container.add(title, salary, requiredExperience, requiredEmployment, descriptionTitle, description, contacts);
        showResponseButton(container);

        add(container);
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }

    private void showResponseButton(VerticalLayout container) {
        responseButton = new Button("Откликнуться");

        responseButton.addClickListener(clickEvent ->
        {
            Optional<Job> jobOp = jobService.getJobById(jobId);
            Optional<Student> studentOp = studentService.getStudentById(studentId);

            StudentResponseModel studentResponseModel = StudentResponseModel.builder()
                    .job(jobOp.get())
                    .student(studentOp.get())
                    .responseDate(LocalDateTime.now())
                    .build();

            studentResponseService.saveStudentResponseModel(studentResponseModel);
        });

        // get student id
        Optional<StudentResponseModel> studentResponseModelOp = studentResponseService.getStudentResponseModelByJobIdAndStudentId(jobId, studentId);
        // if response already exists deactivate button and write message
        if (studentResponseModelOp.isPresent()) {
            responseButton.setEnabled(false);
            responseAlreadySend = new Label("Отклик уже отправлен!");
            responseAlreadySend.addClassNames(LumoUtility.TextColor.SUCCESS);
            container.add(responseAlreadySend);
        }

        responseButton.setDisableOnClick(true);
        container.add(responseButton);
    }


}
