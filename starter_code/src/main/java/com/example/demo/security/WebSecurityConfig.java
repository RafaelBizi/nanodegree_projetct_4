package com.example.demo.security;

import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author RafaelBizi
 * @created 15/11/2021 - 12:04
 * @project nanodegree_project_4
 */

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public WebSecurityConfig(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //We don't need CSRF for this example
                .authorizeRequests()
                .anyRequest().authenticated() // all request requires a logged in user

                .and()
                .formLogin()
                .loginProcessingUrl("/login") //the URL on which the clients should post the login information
                .usernameParameter("login") //the username parameter in the queryString, default is 'username'
                .passwordParameter("password") //the password parameter in the queryString, default is 'password'
                .successHandler(this::loginSuccessHandler)
                .failureHandler(this::loginFailureHandler)

                .and()
                .logout()
                .logoutUrl("/logout") //the URL on which the clients should post if they want to logout
                .logoutSuccessHandler(this::logoutSuccessHandler)
                .invalidateHttpSession(true)

                .and()
                .exceptionHandling() //default response if the client wants to get a resource unauthorized
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }

    private void loginSuccessHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "You logged in!");
    }

    private void loginFailureHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(), "Nopity nop!");
    }

    private void logoutSuccessHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "Bye!");
    }
}
