package com.example.application.ui.company;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.service.CompanyService;
import com.example.application.ui.MainLayout;
import com.example.application.ui.student.StudentPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "company/edit", layout = MainLayout.class)
public class EditCompanyView extends VerticalLayout implements HasUrlParameter<Long> {

    private Long companyId;

    private CompanyService companyService;


    private Label error;
    private H2 title;
    private TextField companyName;
    private TextField phoneNumber;
    private TextField email;
    private TextArea description;

    private Button saveButton;
    private Button backButton;

    @Autowired
    public EditCompanyView(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        this.companyId = id;
        Optional<Company> companyOp = companyService.getCompanyById(id);
        if (companyOp.isEmpty()) {
            showError();
            return;
        }
        drawEditPage(companyOp.get());
    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }

    private void drawEditPage(Company company) {
        VerticalLayout container = new VerticalLayout();

        title = new H2("Редактирование данных студента");
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        companyName = new TextField("Имя");
        companyName.setValue((company.getName() != null) ? company.getName() : "");

        phoneNumber = new TextField("Телефон");
        phoneNumber.setValue((company.getPhoneNumber() != null) ? company.getPhoneNumber() : "");
        email = new TextField("Почта");
        email.setValue((company.getEmail() != null) ? company.getEmail(): "");

        FormLayout formLayout = new FormLayout();
        formLayout.add(companyName, phoneNumber, email);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));


        description = new TextArea();
        description.setWidthFull();
        description.setLabel("Резюме");
        description.setValue((company.getDescription() != null) ? company.getDescription() : "");

        saveButton = new Button("Сохранить изменения");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(clickEvent ->
        {
            if (!populateCompanyNewData(company))
                return;
            updateCompanyData(company);
            Notification.show("Данные компании обновлены!");
        });

        backButton = new Button("Назад");
        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backButton.addClickListener(clickEvent ->
                backButton.getUI().ifPresent(ui ->
                        ui.navigate(CompanyPage.class))
        );

        HorizontalLayout buttonContainer = new HorizontalLayout();
        buttonContainer.add(saveButton, backButton);

        container.add(title, formLayout, description, buttonContainer);
        add(container);
    }

    private boolean populateCompanyNewData(Company company) {
        if (companyName.getValue() == "") {
            Notification.show("Необходимо указать название компании!");
            return false;
        } else company.setName(companyName.getValue());

        if (phoneNumber.getValue() == "") {
            Notification.show("Необходимо указать номер телефона!");
            return false;
        } else company.setPhoneNumber(phoneNumber.getValue());

        if (email.getValue() == "") {
            Notification.show("Необходимо указать электронную почту!");
            return false;
        } else company.setEmail(email.getValue());

        if (description.getValue() == "") {
            Notification.show("Необходимо привести описание компании!");
            return false;
        } else company.setDescription(description.getValue());

        return true;
    }

    private void updateCompanyData(Company company){
        companyService.updateCompany(company);
    }
}
