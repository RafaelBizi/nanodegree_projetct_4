package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RafaelBizi
 * @created 15/11/2021 - 12:03
 * @project nanodegree_project_4
 */

@RestController
@RequestMapping("/test")
public class AuthenticationTestController {
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("You can only see this after a successful login :)");
    }

}
