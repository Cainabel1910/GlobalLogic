package com.globallogic.usuariobci.usuario_bci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidationService {

    private final String passwordRegex;

    @Autowired
    public PasswordValidationService(String passwordRegex) {
        this.passwordRegex = passwordRegex;
    }

    public boolean validatePassword(String password) {
        return password.matches(passwordRegex);
    }

}
