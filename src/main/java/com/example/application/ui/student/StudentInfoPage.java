package com.example.application.ui.student;

import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.StudentService;
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
@Route(value = "student", layout = MainLayout.class)
public class StudentInfoPage extends VerticalLayout implements HasUrlParameter<Long> {

    private Long studentId;
    private StudentService studentService;


    private H2 title;

    private Span name;
    private Span surname;
    private Span courseOfStudy;
    private Span experience;
    private Span employmentType;
    private Span salary;

    private Paragraph resume;

    private Accordion contacts;

    private Button responseButton;

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
                .append(student.getName())
                .append("\n")
                .append("Курс обучения: ")
                .append(student.getCourseOfStudy());

        name = new Span(baseInfoBuilder.toString());
        name.getStyle().set("white-space", "pre-line");
        name.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);
        surname = new Span("Фамилия: " + student.getName());
        surname.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        courseOfStudy = new Span("Курс обучения: " + student.getCourseOfStudy());
        courseOfStudy.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        experience = new Span("Опыт: " + student.getExperience());
        experience.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        employmentType = new Span("Занятость: " + student.getDesiredEmployment());
        employmentType.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        salary = new Span("Заработная плата: " + student.getDesiredSalary() + "₽");
        salary.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);


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

        responseButton = new Button("Отправить приглашение");

        container.add(title,name,surname, courseOfStudy,experience, employmentType, salary, resume,contacts,responseButton);
        add(container);
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }

}
