package com.example.application.ui.admin;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.entities.models.CompanyRegisterRequestModel;
import com.example.application.backend.entities.security.User;
import com.example.application.backend.service.*;
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
import javax.transaction.Transactional;
import java.util.Optional;

@PermitAll
@Route(value = "company-request-info", layout = MainLayout.class)
public class CompanyRequestInfo extends VerticalLayout implements HasUrlParameter<Long> {

    private Long requestId;
    private AccountService accountService;
    private EmailNotificationService emailNotificationService;
    private PasswordGeneratorService passwordGeneratorService;
    private UserService userService;
    private CompanyService companyService;

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
    public CompanyRequestInfo(AccountService accountService, EmailNotificationService emailNotificationService,
                              PasswordGeneratorService passwordGeneratorService, UserService userService,
                              CompanyService companyService) {
        this.accountService = accountService;
        this.emailNotificationService = emailNotificationService;
        this.passwordGeneratorService = passwordGeneratorService;
        this.userService = userService;
        this.companyService = companyService;
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

        createAccountButton = new Button("Одобрить заявку", e -> createAccount(reqModel.getCompanyEmail(), reqModel));
        dismissButton = new Button("Отклонить заявку", e -> dismissAccountCreation(reqModel.getCompanyEmail(), comments.getValue(), reqModel.getRequestId()));
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

    private void createAccount(String login, CompanyRegisterRequestModel request) {
        // generate password and send email to user
        String password = sendEmailWithPassword(login);
        // save new company account
        saveNewCompany(request, password);
        // remove create-company request
        accountService.removeCreateCompanyAccountRequest(request.getRequestId());
        // redirect to company-requests page
        getUI().get().navigate(ListOfCreateCompanyAccountRequestsView.class);
    }

    private void dismissAccountCreation(String email, String comments, Long requestId){
        // send email
        sendDismissEmail(comments, email);
        // remove create-student request
        accountService.removeCreateCompanyAccountRequest(requestId);
        // redirect to student-requests view
        getUI().get().navigate(ListOfCreateCompanyAccountRequestsView.class);
    }


    private String sendEmailWithPassword(String login) {
        String password = passwordGeneratorService.generatePassword(10);
        String message = String.format("Заявка на создание аккаунта одобрена. Логин: %s, Пароль: %s", login, password);
        emailNotificationService.sendSimpleEmail(login, "Создание аккаунта", message);
        return password;
    }

    private void sendDismissEmail(String comments, String login) {
        String message;
        if (comments != null) {

            message = String.format("Заявка на создание аккаунта отклонена. Причина: %s", comments);
        } else
            message = "Заявка на создание аккаунта отклонена";

        emailNotificationService.sendSimpleEmail(login, "Создание аккаунта", message);
    }

    @Transactional
    void saveNewCompany(CompanyRegisterRequestModel request, String password) {
        User user = User.builder()
                .username(request.getCompanyEmail())
                .password(password)
                .build();

        userService.saveCompany(user);

        Company company = Company.builder()
                .user(user)
                .name(request.getCompanyName())
                .description(request.getCompanyDescription())
                .email(request.getCompanyEmail())
                .phoneNumber(request.getCompanyPhoneNumber())
                .build();

        companyService.saveCompany(company);
    }
}
