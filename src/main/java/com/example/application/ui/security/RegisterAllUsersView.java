package com.example.application.ui.security;

import com.example.application.backend.repositories.CreateAccountService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import javax.annotation.security.PermitAll;

@PermitAll
@Route("register-all")
public class RegisterAllUsersView extends VerticalLayout {

    private CreateAccountService createAccountService;

    private TabSheet tabSheet;
    private VerticalLayout registerStudentContainer;
    private VerticalLayout registerCompanyContainer;

    // students
    private TextField studentFirstName;
    private TextField studentLastName;
    private TextField studentDocumentNumber;
    private TextField studentCourseOfStudy;
    private TextField studentPhoneNumber;
    private TextField studentEmail;


    // company
    private TextField companyName;
    private TextField companyPhoneNumber;
    private TextField companyEmail;
    private TextArea companyDescription;


    public RegisterAllUsersView(CreateAccountService createAccountService) {
        this.createAccountService = createAccountService;

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(Alignment.CENTER);

        H1 logo = new H1("Регистрация");
        logo.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        registerStudentContainer = new VerticalLayout();
        registerCompanyContainer = new VerticalLayout();

        createRegisterStudentLayout();
        createRegisterCompanyLayout();

        tabSheet = new TabSheet();
        tabSheet.setWidthFull();
        tabSheet.add("Регистрация аккаунта студента",
                registerStudentContainer);
        tabSheet.add("Регистрация аккаунта компании",
                registerCompanyContainer);

        HorizontalLayout buttonContainer = new HorizontalLayout();
        buttonContainer.setAlignItems(Alignment.START);
        buttonContainer.setWidthFull();
        Button registerButton = new Button("Отправить данные на проверку", e -> register());

        buttonContainer.add(registerButton);


        container.add(logo, tabSheet, buttonContainer);
        add(container);
    }

    private void createRegisterStudentLayout() {
        Span info = new Span("После заполнения формы корректность данных будет проверена администрацией сайта, в случае успешной проверки логин и пароль от личного кабинета будут высланы Вам на указанную электронную почту");
        info.addClassNames(LumoUtility.TextColor.SECONDARY);

        String nameRegex = "^[A-ZА-Я][a-zа-я]+(-[A-ZА-Я][a-zа-я]+)*$";
        studentFirstName = new TextField("Имя");
        studentFirstName.setPattern(nameRegex);
        studentFirstName.setHelperText("Формат: Иван");


        studentLastName = new TextField("Фамилия");
        studentLastName.setPattern(nameRegex);
        studentFirstName.setHelperText("Формат: Иванович");

        studentDocumentNumber = new TextField("Номер студенческого билета");

        String regexCourse = "[1-6]";
        studentCourseOfStudy = new TextField("Курс обучения");
        studentCourseOfStudy.setPattern(regexCourse);
        studentCourseOfStudy.setHelperText("Формат: цифра от 1 до 6");

        String regexPhoneNumber = "\\+7\\d{10}";
        studentPhoneNumber = new TextField("Телефон");
        studentPhoneNumber.setPattern(regexPhoneNumber);
        studentPhoneNumber.setHelperText("Формат: +79766333800");

        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        ;
        studentEmail = new TextField("Почта");
        studentEmail.setPattern(regexEmail);
        studentEmail.setHelperText("Формат: testemail@gmail.com");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));
        formLayout.add(studentFirstName, studentLastName, studentDocumentNumber, studentCourseOfStudy, studentPhoneNumber, studentEmail);
        registerStudentContainer.add(info, formLayout);
    }

    private void createRegisterCompanyLayout() {
        Span info = new Span("После заполнения формы корректность данных будет проверена администрацией сайта, в случае успешной проверки логин и пароль от личного кабинета будут высланы Вам на указанную электронную почту");
        info.addClassNames(LumoUtility.TextColor.SECONDARY);

        String nameRegex = "^^[a-zA-Z]+( [a-zA-Z]+)*$";
        companyName = new TextField("Название компании");
        companyName.setPattern(nameRegex);
        companyName.setHelperText("Формат: Yandex");


        String regexPhoneNumber = "\\+7\\d{10}";
        companyPhoneNumber = new TextField("Телефон");
        companyPhoneNumber.setPattern(regexPhoneNumber);
        companyPhoneNumber.setHelperText("Формат: +79766333800");

        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        ;
        companyEmail = new TextField("Почта");
        companyEmail.setPattern(regexEmail);
        companyEmail.setHelperText("Формат: testemail@gmail.com");

        companyDescription = new TextArea("Описание компании");
        companyDescription.setWidthFull();

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));
        formLayout.add(companyName, companyPhoneNumber, companyEmail);
        registerCompanyContainer.add(info, formLayout, companyDescription);
    }

    private void register() {
        Tab tab = tabSheet.getSelectedTab();
        String selectedTabLable = tab.getLabel();

        boolean correctStatus = false;
        int type = 0;

        if (selectedTabLable.equals("Регистрация аккаунта студента")) {
            correctStatus = checkStudentDataCorrect();
            type = 1;
        } else {
            correctStatus = checkCompanyDataCorrect();
            type = 2;
        }

        if (!correctStatus) {
            Notification.show("Данные заполнены неверно");
            return;
        }

        saveRequest(type);
    }

    private boolean checkStudentDataCorrect() {
        if (studentFirstName.isEmpty() || studentFirstName.isInvalid()) return false;
        if (studentLastName.isEmpty() || studentLastName.isInvalid()) return false;
        if (studentCourseOfStudy.isEmpty() || studentCourseOfStudy.isInvalid()) return false;
        if (studentEmail.isEmpty() || studentEmail.isInvalid()) return false;
        if (studentPhoneNumber.isEmpty() || studentPhoneNumber.isInvalid()) return false;

        return true;
    }

    private boolean checkCompanyDataCorrect() {
        if (companyName.isEmpty() || companyName.isInvalid()) return false;
        if (companyPhoneNumber.isEmpty() || companyPhoneNumber.isInvalid()) return false;
        if (companyEmail.isEmpty() || companyEmail.isInvalid()) return false;
        if (companyDescription.isEmpty() || companyDescription.isInvalid()) return false;

        return true;
    }

    private void saveRequest(int type) {
        switch (type) {
            case 1: {
                createAccountService.createStudentAccount(studentFirstName.getValue(),
                        studentLastName.getValue(),
                        studentDocumentNumber.getValue(),
                        Integer.valueOf(studentCourseOfStudy.getValue()),
                        studentPhoneNumber.getValue(),
                        studentEmail.getValue()
                );
                break;
            }
            case 2: {
                createAccountService.createCompanyAccount(companyName.getValue(),
                        companyPhoneNumber.getValue(),
                        companyEmail.getValue(),
                        companyDescription.getValue());
                break;
            }
        }

        UI.getCurrent().navigate(CustomLoginView.class);
        Notification.show("Заявка отправлена. Ожидайте ответа на почту!");
    }

}
