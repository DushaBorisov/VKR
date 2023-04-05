package com.example.application.ui.security;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import javax.annotation.security.PermitAll;

@PermitAll
@Route("register-all")
public class RegisterAllUsersView extends VerticalLayout {
    private TabSheet tabSheet;

    public RegisterAllUsersView() {

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(Alignment.CENTER);

        H1 logo = new H1("Регистрация");
        logo.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);
        tabSheet = new TabSheet();
        tabSheet.setWidthFull();
        tabSheet.add("Регистрация аккаунта студента",
                new Div(new Text("This is the Dashboard tab content")));
        tabSheet.add("Регистрация аккаунта компании",
                new Div(new Text("This is the Payment tab content")));

        container.add(logo,tabSheet);
        add(container);
    }
}
