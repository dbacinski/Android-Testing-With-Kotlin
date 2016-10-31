package com.example.unittesting.model.login;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class LoginUseCase {

    LoginService loginService;

    public LoginUseCase(LoginService loginService) {
        this.loginService = loginService;
    }

    public Observable<Boolean> loginWithCredentialsWithStatus(final LoginCredentials credentials) {
        checkNotNull(credentials);
        return Observable.fromCallable(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return loginService.login(credentials.login, credentials.password);
                    }
                }
        ).delay(1000, TimeUnit.MILLISECONDS);
    }

    private void checkNotNull(LoginCredentials credentials) {
        if (credentials == null) {
            throw new NullPointerException("Credentials cannot be null");
        }
    }
}
