package com.msrfyl.k24.oauth2.security;

import kotlin.jvm.Throws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {

    @Autowired
    private MyAuthProvider provider;

    @Bean
    public SecurityFilterChain defaultSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authenticationProvider(provider)
                .authorizeHttpRequests(i -> {
                   i.antMatchers("/login").permitAll()
                           .antMatchers("/bg").permitAll()
                           .anyRequest().authenticated();
                })
                .formLogin(i -> {
                    i.loginPage("/login")
                            .failureUrl("/login?error=true");
                });
        return httpSecurity.build();
    }

}
