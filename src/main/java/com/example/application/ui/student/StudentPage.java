package com.example.application.ui.student;

import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.StudentService;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "user", layout = MainLayout.class)
public class StudentPage extends VerticalLayout {

    private UserContext userContext;
    private StudentService studentService;

    private Span courseOfStudy;
    private Span desiredPosition;
    private Span desiredSalary;
    private H2 title;

    private Span descriptionTitle;
    private Paragraph description;

    private Accordion contacts;


    private Label error;
    private Button editButton;


    @Autowired
    public StudentPage(UserContext userContext, StudentService studentService) {
        this.userContext =userContext;
        this.studentService = studentService;

        addClassName("person-form-view");

        String sessionId = VaadinSession.getCurrent().getSession().getId();
        Optional<UserData> useData = userContext.getAuthenticatedUser(sessionId);
        String username = useData.get().getUserName();

        Optional<Student> studentOp = studentService.getStudentByUsername(username);
        if (studentOp.isPresent())
            drawStudentInfo(studentOp.get());
        else showError();
    }

    private void drawStudentInfo(Student student) {
        VerticalLayout container = new VerticalLayout();

        title = new H2(student.getName() + " " + student.getSurname());
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        courseOfStudy = new Span("Курс обучения: " + student.getCourseOfStudy());
        courseOfStudy.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        desiredPosition = new Span("Желаемая позиция: " + student.getDesiredPosition());
        desiredPosition.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        desiredSalary = new Span("Желаемая заработная плата: " + student.getDesiredSalary() + "₽");
        desiredSalary.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        descriptionTitle = new Span("Резюме: ");
        descriptionTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.BLACK);

        description = new Paragraph(student.getResume());
        description.getStyle().set("white-space", "pre-line");
        description.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);

        contacts = new Accordion();
        Span email = new Span(student.getEmail());
        Span phone = new Span(student.getPhone());
        VerticalLayout personalInformationLayout = new VerticalLayout(email, phone);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);
        contacts.add("Контактная информация", personalInformationLayout);


        editButton = new Button("Редактировать данные");
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(clickEvent ->
        {
            editButton.getUI().ifPresent(ui ->
                    ui.navigate(EditStudentView.class, student.getStudentId()));
        });

        container.add(title, desiredPosition,desiredSalary, courseOfStudy, descriptionTitle, description, contacts);

        add(container, editButton);

    }


    private void showError() {
        error = new Label("Error");
        add(error);
    }
}
