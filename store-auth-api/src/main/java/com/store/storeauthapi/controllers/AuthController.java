package com.store.storeauthapi.controllers;

import java.util.Map;

import com.store.storeauthapi.models.LoginCredentials;
import com.store.storeauthapi.services.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final LoginService loginService;

    @Autowired
    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public Map<String, ?> login(@RequestBody LoginCredentials loginCredentials) {

        return loginService.login(loginCredentials.getUsername(), loginCredentials.getPassword());
    }

}
