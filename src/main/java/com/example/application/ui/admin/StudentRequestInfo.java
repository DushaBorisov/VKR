package com.example.application.ui.admin;

import com.example.application.backend.entities.models.StudentRegisterRequestModel;
import com.example.application.backend.service.AccountService;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "student-request-info", layout = MainLayout.class)
public class StudentRequestInfo extends VerticalLayout implements HasUrlParameter<Long> {

    private Long requestId;
    private AccountService accountService;

    private Label error;

    private H2 title;
    private Span curseOfStudy;
    private Span numberOfStudentDocument;
    private Span studentPhoneNumber;
    private Span studentEmail;
    private Span commentsLable;
    private TextArea comments;

    private Button createAccountButton;
    private Button dismissButton;


    @Autowired
    public StudentRequestInfo(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.requestId = id;
        createView(id);
    }

    public void createView(Long reqId) {
        Optional<StudentRegisterRequestModel> studentAccReqOp = accountService.getStudentRequestModelById(reqId);

        if (studentAccReqOp.isEmpty()) showError();
        StudentRegisterRequestModel reqModel = studentAccReqOp.get();

        VerticalLayout container = new VerticalLayout();

        title = new H2(reqModel.getStudentFirstName() + reqModel.getStudentLastName());
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        curseOfStudy = new Span("Курс обучения: " + reqModel.getStudentCourseOfStudy());
        curseOfStudy.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        numberOfStudentDocument = new Span("Номер студенческого билета: " + reqModel.getStudentDocumentNumber());
        numberOfStudentDocument.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        studentPhoneNumber = new Span("Номер телефона: " + reqModel.getStudentPhoneNumber());
        studentPhoneNumber.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        studentEmail = new Span("Электронная почта: " + reqModel.getStudentEmail());
        studentEmail.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        comments = new TextArea("Укажите при необходимости комментарии к ответу на заявку пользователя, пользователь их увидит в электронном письме:");
        comments.setWidthFull();

        createAccountButton = new Button("Одобрить заявку");
        dismissButton = new Button("Отклонить заявку");
        dismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttonContainer = new HorizontalLayout();
        buttonContainer.add(createAccountButton, dismissButton);

        container.add(title, curseOfStudy, numberOfStudentDocument, studentPhoneNumber, studentEmail, comments, buttonContainer);
        add(container);
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }
}
