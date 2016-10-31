package com.example.unittesting.domain.login;

import com.example.unittesting.entity.login.LoginCredentials;
import com.example.unittesting.entity.login.LoginRepository;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class LoginUseCase {

    LoginRepository loginRepository;

    public LoginUseCase(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Observable<Boolean> loginWithCredentialsWithStatus(final LoginCredentials credentials) {
        checkNotNull(credentials);
        return Observable.fromCallable(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return loginRepository.login(credentials.login, credentials.password);
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
