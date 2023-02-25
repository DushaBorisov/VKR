package com.example.application.security;

import com.example.application.ui.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private static class SimpleInMemoryUserDetailsManager extends InMemoryUserDetailsManager {

        public SimpleInMemoryUserDetailsManager() {
            createUser(new User("user_name_1",
                    "{noop}userpass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            ));
            createUser(new User("company_user_name_1",
                    "{noop}pass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            ));
            createUser(new User("company_user_name_2",
                    "{noop}pass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            ));
            createUser(new User("company_user_name_3",
                    "{noop}pass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            ));
            createUser(new User("company_user_name_4",
                    "{noop}pass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            ));
            createUser(new User("company_user_name_5",
                    "{noop}pass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            ));
            createUser(new User("company_user_name_6",
                    "{noop}pass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            ));
            createUser(new User("company_user_name_7",
                    "{noop}pass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_COMPANY"))
            ));
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/images/**").permitAll();

        super.configure(http);

        setLoginView(http, LoginView.class);
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        return new SimpleInMemoryUserDetailsManager();
    }
}
