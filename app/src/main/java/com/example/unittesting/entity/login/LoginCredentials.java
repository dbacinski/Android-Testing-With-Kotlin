package com.example.unittesting.entity.login;

public class LoginCredentials {
    public String login;
    public String password;

    public LoginCredentials withLogin(String login) {
        this.login = login;
        return this;
    }

    public LoginCredentials withPassword(String password) {
        this.password = password;
        return this;
    }
}
