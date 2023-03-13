package com.example.application.ui.student;

import com.example.application.backend.entities.models.Student;
import com.example.application.backend.service.StudentService;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "user/edit", layout = MainLayout.class)
public class EditStudentView extends VerticalLayout implements HasUrlParameter<Long> {

    private TextArea resume;
    private Button saveButton;
    private Button backButton;
    private Label error;
    private StudentService studentService;

    private Long studentId;

    @Autowired
    public EditStudentView(StudentService studentService) {
        this.studentService = studentService;
    }

    private void drawEditPage(Student student) {
        VerticalLayout container = new VerticalLayout();

        resume = new TextArea();
        resume.setWidthFull();
        resume.setLabel("Резюме");
        resume.setValue(student.getResume());

        saveButton = new Button("Сохранить изменения");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(clickEvent ->
        {
            populateStudentNewData(student);
            updateStudentData(student);
        });

        backButton = new Button("Назад");
        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backButton.addClickListener( clickEvent ->
             backButton.getUI().ifPresent(ui ->
                     ui.navigate(StudentPage.class))
        );

        HorizontalLayout buttonContainer = new HorizontalLayout();
        buttonContainer.add(saveButton, backButton);

        container.add(resume, buttonContainer);
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

    private void populateStudentNewData(Student student){
        student.setResume(resume.getValue());
    }

    private void updateStudentData(Student student){
        studentService.updateStudent(student);
    }
}
