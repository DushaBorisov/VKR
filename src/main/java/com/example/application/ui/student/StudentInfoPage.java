package com.example.application.ui.student;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.CompanyResponseModel;
import com.example.application.backend.entities.models.Job;
import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.CompanyResponseService;
import com.example.application.backend.service.CompanyService;
import com.example.application.backend.service.JobService;
import com.example.application.backend.service.StudentService;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@PermitAll
@Route(value = "student", layout = MainLayout.class)
public class StudentInfoPage extends VerticalLayout implements HasUrlParameter<Long> {

    private Long studentId;
    private Long companyId;
    private StudentService studentService;
    private CompanyResponseService companyResponseService;
    private JobService jobService;
    private CompanyService companyService;
    private UserContext userContext;


    private H2 title;
    private H1 resumeTitle;

    private Span baseInfo;
    private Span surname;
    private Span courseOfStudy;
    private Span experience;
    private Span employmentType;
    private Span salary;

    private Paragraph resume;

    private Accordion contacts;

    private Button responseButton;
    private Button moveBackButton;

    private Label error;
    Select<String> jobVariantForStudent;

    @Autowired
    public StudentInfoPage(StudentService studentServ, CompanyResponseService companyResponseService,
                           JobService jobService, UserContext userContext, CompanyService companyService) {
        this.studentService = studentServ;
        this.companyResponseService = companyResponseService;
        this.jobService = jobService;
        this.userContext = userContext;
        this.companyService = companyService;
        addClassName("person-form-view");


        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        String username = useData.get().getUserName();

        Optional<Company> companyOp = companyService.getCompanyByUserName(username);
        companyId = companyOp.get().getCompanyId();

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.studentId = id;
        createView(studentId);
    }

    public void createView(Long studentId) {
        Optional<Student> studentOp = studentService.getStudentById(studentId);
        if (studentOp.isEmpty()) {
            showError();
            return;
        }

        Student student = studentOp.get();

        VerticalLayout container = new VerticalLayout();

        title = new H2(student.getDesiredPosition());
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        StringBuilder baseInfoBuilder = new StringBuilder();
        baseInfoBuilder.append("Имя: ")
                .append(student.getName())
                .append("\n")
                .append("Фамилия: ")
                .append(student.getSurname())
                .append("\n")
                .append("Курс обучения: ")
                .append(student.getCourseOfStudy())
                .append("\n")
                .append("Опыт: ")
                .append(student.getExperience())
                .append("\n")
                .append("Занятость: ")
                .append(student.getDesiredEmployment())
                .append("\n")
                .append("Заработная плата: ")
                .append(student.getDesiredSalary() + "₽")
                .append("\n");

        baseInfo = new Span(baseInfoBuilder.toString());
        baseInfo.getStyle().set("white-space", "pre-line");
        baseInfo.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        resumeTitle = new H1("Резюме:");
        resumeTitle.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.LARGE);
        resume = new Paragraph(student.getResume());
        resume.getStyle().set("white-space", "pre-line");
        resume.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);


        contacts = new Accordion();
        Span email = new Span(student.getEmail());
        Span phone = new Span(student.getPhone());
        VerticalLayout personalInformationLayout = new VerticalLayout(email, phone);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);
        contacts.add("Контактная информация", personalInformationLayout);


        List<Job> jobList = jobService.getAllCompanyJobsByCompanyId(companyId);
        jobVariantForStudent = new Select<>();
        jobVariantForStudent.setLabel("Предлагаемая вакансия");
        jobVariantForStudent.setItems(jobList.stream().map(job -> job.getJobTitle()));

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        showResponseButton(buttonsContainer, jobList);
        moveBackButton = new Button("Назад", e -> getUI().get().navigate(ListOfStudents.class));
        moveBackButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonsContainer.add( moveBackButton);


        container.add(title, baseInfo, resumeTitle, resume, contacts, jobVariantForStudent, buttonsContainer);
        add(container);
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }

    private void showResponseButton(HorizontalLayout buttonsContainer,  List<Job> jobList ){
        responseButton = new Button("Отправить приглашение", e -> {
            String selectedJobDescription = jobVariantForStudent.getValue();
            Optional<Job> jobOp = jobList.stream().filter(job -> job.getJobTitle().equals(selectedJobDescription)).findFirst();
            saveCompanyResponse(jobOp.get().getJobId());
        });


        Optional<CompanyResponseModel> cresp = companyResponseService.getCompanyResponseModelByCompanyIdAndStudentId(companyId, studentId);
        if(cresp.isPresent()){
            responseButton.setEnabled(false);
        }
        responseButton.setDisableOnClick(true);
        buttonsContainer.add(responseButton);
    }

    @Transactional
    public void saveCompanyResponse(Long jobId) {
        Optional<Job> jobOp = jobService.getJobById(jobId);
        Optional<Student> studentOp = studentService.getStudentById(studentId);

        CompanyResponseModel companyResponseModel = CompanyResponseModel.builder()
                .job(jobOp.get())
                .student(studentOp.get())
                .responseDate(LocalDateTime.now())
                .build();

        companyResponseService.saveCompanyResponseModel(companyResponseModel);
    }

}
