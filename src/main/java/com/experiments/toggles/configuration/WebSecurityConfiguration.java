package com.experiments.toggles.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // protect all requests with an authenticated ADMIN user
        http
            .authorizeRequests()
                .anyRequest().hasRole(Roles.ADMIN.name()).and()
                .httpBasic().and()
            .logout()
                .permitAll();

        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // in-memory users can easily be replaced by a jdbc source or other
        auth
            .inMemoryAuthentication()
                .withUser("admin").password("admin").roles(Roles.ADMIN.name());

        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles(Roles.OPERATOR.name());
    }

    enum Roles {
        ADMIN,
        OPERATOR
    }
}