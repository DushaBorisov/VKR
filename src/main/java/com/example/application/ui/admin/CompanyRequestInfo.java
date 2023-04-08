package com.example.application.ui.admin;

import com.example.application.backend.entities.models.CompanyRegisterRequestModel;
import com.example.application.backend.service.AccountService;
import com.example.application.backend.service.EmailNotificationService;
import com.example.application.backend.service.PasswordGeneratorService;
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
@Route(value = "company-request-info", layout = MainLayout.class)
public class CompanyRequestInfo extends VerticalLayout implements HasUrlParameter<Long> {

    private Long requestId;
    private AccountService accountService;
    private EmailNotificationService emailNotificationService;
    private PasswordGeneratorService passwordGeneratorService;

    private Label error;

    private H2 title;
    private Span curseOfStudy;
    private Span numberOfStudentDocument;
    private Span studentPhoneNumber;
    private Span studentEmail;
    private Paragraph description;
    private Span commentsLable;
    private TextArea comments;

    private Button createAccountButton;
    private Button dismissButton;


    @Autowired
    public CompanyRequestInfo(AccountService accountService, EmailNotificationService emailNotificationService, PasswordGeneratorService passwordGeneratorService) {
        this.accountService = accountService;
        this.emailNotificationService = emailNotificationService;
        this.passwordGeneratorService = passwordGeneratorService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.requestId = id;
        createView(id);
    }

    public void createView(Long reqId) {
        Optional<CompanyRegisterRequestModel> companyAccReqOp = accountService.getCompanyRequestModelById(reqId);

        if (companyAccReqOp.isEmpty()) showError();
        CompanyRegisterRequestModel reqModel = companyAccReqOp.get();

        VerticalLayout container = new VerticalLayout();

        title = new H2(reqModel.getCompanyName());
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        description = new Paragraph(reqModel.getCompanyDescription());
        description.getStyle().set("white-space", "pre-line");
        description.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);


        studentPhoneNumber = new Span("Номер телефона: " + reqModel.getCompanyPhoneNumber());
        studentPhoneNumber.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        studentEmail = new Span("Электронная почта: " + reqModel.getCompanyEmail());
        studentEmail.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        comments = new TextArea("Укажите при необходимости комментарии к ответу на заявку пользователя, пользователь их увидит в электронном письме:");
        comments.setWidthFull();

        createAccountButton = new Button("Одобрить заявку", e -> createAccount(reqModel.getCompanyEmail()));
        dismissButton = new Button("Отклонить заявку", e -> dismiss(comments.getValue(), reqModel.getCompanyEmail()));
        dismissButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttonContainer = new HorizontalLayout();
        buttonContainer.add(createAccountButton, dismissButton);

        container.add(title, description, studentPhoneNumber, studentEmail, comments, buttonContainer);
        add(container);
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }

    private void createAccount(String login) {
        String password = passwordGeneratorService.generatePassword(10);
        String message = String.format("Заявка на создание аккаунта одобрена. Логин: %s, Пароль: %s", login, password);
        emailNotificationService.sendSimpleEmail(login, "Создание аккаунта", message);
    }

    private void dismiss(String comments, String login) {
        String message;
        if (comments != null) {

            message = String.format("Заявка на создание аккаунта отклонена. Причина: %s", comments);
        } else
            message = "Заявка на создание аккаунта отклонена";

        emailNotificationService.sendSimpleEmail(login, "Создание аккаунта", message);
    }
}
