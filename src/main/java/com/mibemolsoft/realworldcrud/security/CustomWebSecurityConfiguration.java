package com.mibemolsoft.realworldcrud.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfiguration {

    // TODO: Fix all this , get authentication details from the database, populate the database
    @Autowired
    private DataSource dataSource;

    // We need to move this to another config file due to a bug causing circular dependencies
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser("user")
                    .password(passwordEncoder.encode("password"))
                    .roles("USER");
    }

}


