package com.example.unittesting.model.login;

public class LoginUseCase {

    LoginService loginService;

    public LoginUseCase(LoginService loginService) {
        this.loginService = loginService;
    }

    public boolean loginWithCredentialsWithStatus(LoginCredentials credentials) {
        checkNotNull(credentials);
        return loginService.login(credentials.login, credentials.password);
    }

    private void checkNotNull(LoginCredentials credentials) {
        if (credentials == null) {
            throw new NullPointerException("Credentials cannot be null");
        }
    }
}
