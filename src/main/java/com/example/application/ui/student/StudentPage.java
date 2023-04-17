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

    private H2 title;
    private Span baseInfo;

    private Span descriptionTitle;
    private Paragraph description;
    private Span notFullAccountErrorTitle;

    private Accordion contacts;


    private Label error;
    private Button editButton;
    private Button moveBackButton;


    @Autowired
    public StudentPage(UserContext userContext, StudentService studentService) {
        this.userContext = userContext;
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

        StringBuilder baseInfoBuilder = new StringBuilder();
        baseInfoBuilder
                .append("Желаемая позиция: ")
                .append((student.getDesiredPosition() == null) ? "-" : student.getDesiredPosition())
                .append("\n")
                .append("Желаемая заработная плата:  ")
                .append((student.getDesiredSalary() == null) ? "-" : student.getDesiredSalary() + "₽")
                .append("\n")
                .append("Занятость: ")
                .append((student.getDesiredEmployment() == null) ? "-" : student.getDesiredEmployment())
                .append("\n")
                .append("Опыт: ")
                .append((student.getExperience() == null) ? "-" : student.getExperience())
                .append("\n");

        baseInfo = new Span(baseInfoBuilder.toString());
        baseInfo.getStyle().set("white-space", "pre-line");
        baseInfo.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        descriptionTitle = new Span("Резюме: ");
        descriptionTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.BLACK);

        description = new Paragraph((student.getResume() == null) ? "данные отсутствуют" : student.getResume());
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

        container.add(title);

        if (!checkIsAccountFull(student)) {
            notFullAccountErrorTitle = new Span("Персональная информация не заполнена до конца. " + "\n" +
                    "Пока все данные не будут указаны, резюме не появится в списке у работодателей!");
            notFullAccountErrorTitle.getStyle().set("white-space", "pre-line");
            notFullAccountErrorTitle.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.SMALL, LumoUtility.TextColor.ERROR, LumoUtility.FontWeight.LIGHT);
            container.add(notFullAccountErrorTitle);
        }
        container.add(baseInfo, descriptionTitle, description, contacts);
        add(container, editButton);
    }


    private void showError() {
        error = new Label("Error");
        add(error);
    }

    private boolean checkIsAccountFull(Student student) {
        if (student.getDesiredPosition() == null) return false;
        if (student.getDesiredSalary() == null) return false;
        if (student.getResume() == null) return false;
        if (student.getDesiredEmployment() == null) return false;
        return true;
    }
}
