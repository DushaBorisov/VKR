package com.example.application.ui.student;

import com.example.application.backend.entities.enums.EmploymentEnum;
import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.StudentService;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.Optional;

@PermitAll
@Route(value = "user/edit", layout = MainLayout.class)
public class EditStudentView extends VerticalLayout implements HasUrlParameter<Long> {

    private H2 title;
    private TextArea resume;
    private Button saveButton;
    private Button backButton;
    private Label error;

    TextField firstName;
    TextField lastName;
    TextField desiredPosition;
    TextField desiredSalary;
    TextField courseOfStudy;
    TextField phoneNumber;
    TextField email;
    Select<String> desiredEmployment;
    TextField experience;

    private StudentService studentService;

    private Long studentId;

    @Autowired
    public EditStudentView(StudentService studentService) {
        this.studentService = studentService;
    }

    private void drawEditPage(Student student) {
        VerticalLayout container = new VerticalLayout();

        title = new H2("Редактирование данных студента");
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        firstName = new TextField("Имя");
        firstName.setValue((student.getName() != null) ? student.getName() : "");
        lastName = new TextField("Фамилия");
        lastName.setValue((student.getSurname() != null) ? student.getSurname() : "");
        desiredPosition = new TextField("Желаемая должность");
        desiredPosition.setValue((student.getDesiredPosition() != null) ? student.getDesiredPosition() : "");
        desiredSalary = new TextField("Желаемая заработная плата:");
        desiredSalary.setValue((student.getDesiredSalary() != null) ? student.getDesiredSalary().toString() : "");
        courseOfStudy = new TextField("Курс обучения");
        courseOfStudy.setValue((student.getCourseOfStudy() != null) ? student.getCourseOfStudy() : "");

        desiredEmployment = new Select<>();
        desiredEmployment.setLabel("Тип занятости");
        desiredEmployment.setItems(Arrays.stream(EmploymentEnum.values()).map(en -> en.getEmploymentType()));
        experience = new TextField("Опыт");
        experience.setValue((student.getExperience() != null) ? student.getExperience().toString() : "");

        phoneNumber = new TextField("Телефон");
        phoneNumber.setValue(student.getPhone());
        email = new TextField("Почта");
        email.setValue(student.getEmail());

        resume = new TextArea();
        resume.setWidthFull();
        resume.setLabel("Резюме");
        resume.setValue((student.getResume() != null) ? student.getResume() : "");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, desiredPosition, desiredSalary, courseOfStudy, desiredEmployment, experience, phoneNumber, email);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));


        saveButton = new Button("Сохранить изменения");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(clickEvent ->
        {
            populateStudentNewData(student);
            updateStudentData(student);
            Notification.show("Данные студента обновлены!");
        });

        backButton = new Button("Назад");
        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backButton.addClickListener(clickEvent ->
                backButton.getUI().ifPresent(ui ->
                        ui.navigate(StudentPage.class))
        );


        HorizontalLayout buttonContainer = new HorizontalLayout();
        buttonContainer.add(saveButton, backButton);

        container.add(title, formLayout, resume, buttonContainer);
        add(container);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.studentId = id;
        Optional<Student> studentOp = studentService.getStudentById(id);
        if (studentOp.isEmpty()) {
            showError();
            return;
        }
        drawEditPage(studentOp.get());
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }

    private void populateStudentNewData(Student student) {
        student.setResume(resume.getValue());
        student.setName(firstName.getValue());
        student.setSurname(lastName.getValue());
        student.setDesiredPosition(desiredPosition.getValue());
        student.setDesiredSalary(Integer.valueOf(desiredSalary.getValue()));
        student.setCourseOfStudy(courseOfStudy.getValue());
        student.setEmail(email.getValue());
        student.setPhone(phoneNumber.getValue());
        student.setExperience(Integer.valueOf(experience.getValue()));
        student.setDesiredEmployment(desiredEmployment.getValue());
    }

    private void updateStudentData(Student student) {
        studentService.updateStudent(student);
    }
}
