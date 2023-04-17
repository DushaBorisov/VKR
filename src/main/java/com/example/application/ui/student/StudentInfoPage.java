package com.example.application.ui.student;

import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.StudentService;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "student", layout = MainLayout.class)
public class StudentInfoPage extends VerticalLayout implements HasUrlParameter<Long> {

    private Long studentId;
    private StudentService studentService;


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

    @Autowired
    public StudentInfoPage(StudentService studentServ) {
        this.studentService = studentServ;
        addClassName("person-form-view");

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.studentId = id;
        createView(studentId);
    }

    public void createView(Long studentId) {
        Optional<Student> studentOp = studentService.getStudentById(studentId);
        if (studentOp.isEmpty()) showError();
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


        HorizontalLayout buttonsContainer = new HorizontalLayout();
        responseButton = new Button("Отправить приглашение");
        moveBackButton = new Button("Назад", e -> getUI().get().navigate(ListOfStudents.class));
        moveBackButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonsContainer.add(responseButton,  moveBackButton);


        container.add(title, baseInfo, resumeTitle, resume, contacts, buttonsContainer);
        add(container);
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }

}
