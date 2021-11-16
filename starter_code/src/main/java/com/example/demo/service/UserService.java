package com.example.demo.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author RafaelBizi
 * @created 15/11/2021 - 12:16
 * @project nanodegree_project_4
 */

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if("user".equals(login)) {
            return new User(login, "password", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }
    }

}
