package com.example.unittesting.entity.login;

public class LoginValidator {

    static final String EMPTY = "";
    static final int MIN_PASSWORD_LENGTH = 6;

    public boolean validateLogin(String login) {
        return !login.equals(EMPTY);
    }

    public boolean validatePassword(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }
}
