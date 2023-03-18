package com.example.application.ui.company;

import com.example.application.backend.entities.models.Company;
import com.example.application.backend.service.CompanyService;
import com.example.application.security.SecurityService;
import com.example.application.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PermitAll
@Route(value = "company", layout = MainLayout.class)
public class CompanyPage extends VerticalLayout {

    private CompanyService companyService;
    private SecurityService securityService;

    private H2 title;
    private Paragraph description;
    private Label error;
    private Button editButton;


    @Autowired
    public CompanyPage(SecurityService securityService, CompanyService companyService) {
        this.securityService = securityService;
        this.companyService = companyService;

        UserDetails userDetails = securityService.getAuthenticatedUser();
        String username = userDetails.getUsername();

        Optional<Company> companyOp = companyService.getCompanyByUserName(username);
        if (companyOp.isPresent())
            drawCompanyInfo(companyOp.get());
        else showError();
    }

    private void drawCompanyInfo(Company company) {
        VerticalLayout container = new VerticalLayout();
        title = new H2(company.getName());
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);
        description = new Paragraph(company.getDescription());
        description.getStyle().set("white-space", "pre-line");
        description.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);

        editButton = new Button("Редактировать данные");
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        container.add(title, description);

        add(container, editButton);

    }

    private void showError() {
        error = new Label("Error");
        add(error);
    }
}
