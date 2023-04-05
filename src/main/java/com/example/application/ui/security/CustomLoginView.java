package com.example.application.ui.security;

import com.example.application.backend.entities.security.User;
import com.example.application.backend.service.UserService;
import com.example.application.security.UserContext;
import com.example.application.security.UserData;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route(value = "login-custom")
@PageTitle("Login")
public class CustomLoginView extends VerticalLayout {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;

    private final UserService userService;
    private final UserContext userContext;

    @Autowired
    public CustomLoginView(UserService userService, UserContext userContext) {
        this.userService = userService;
        this.userContext = userContext;
        H1 logo = new H1("Вход в систему");
        logo.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXXLARGE);

        VerticalLayout container = new VerticalLayout();
        HorizontalLayout buttonsHolder = new HorizontalLayout();
        //настройка компонентов
        usernameField = new TextField("Логин");
        passwordField = new PasswordField("Пароль");
        loginButton = new Button("Войти", e -> login());
        registerButton = new Button("Зарегистрироваться", e -> register());
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("login-view");

        buttonsHolder.add(loginButton, registerButton);
        buttonsHolder.setAlignItems(Alignment.CENTER);
        container.add(logo,usernameField, passwordField, buttonsHolder);
        container.setAlignItems(Alignment.CENTER);

        //добавление компонентов на страницу
        add(container);
    }

    private void login() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        //валидация логина и пароля
        if (username.isEmpty() || password.isEmpty()) {
            Notification.show("Введите логин и пароль");
            return;
        }

        Optional<User> userOp = userService.getByUserName(username);
        if (userOp.isEmpty()) {
            Notification.show("Пользователь с указанным логином не существует");
            return;
        }
        if (!userOp.get().getPassword().equals(password)) {
            Notification.show("Неверный пароль");
            return;
        }

        String sessionId = VaadinSession.getCurrent().getSession().getId();

        this.userContext.addUserToContext(
                sessionId,
                UserData.builder()
                        .userName(userOp.get().getUsername())
                        .password(userOp.get().getPassword())
                        .role(userOp.get().getRole().getName())
                        .build()
        );
        getUI().get().navigate(BasePage.class);
    }

   private void register(){
       getUI().get().navigate(RegisterAllUsersView.class);
   }


}
