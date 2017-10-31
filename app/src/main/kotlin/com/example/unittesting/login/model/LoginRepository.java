package com.example.unittesting.login.model;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import timber.log.Timber;

public class LoginRepository {

    static final String CORRECT_LOGIN = "dbacinski";
    static final String CORRECT_PASSWORD = "correct";

    public Observable<Boolean> login(String login, String password) {
        Timber.v("login %s with password %s", login, password);

        return Observable.just(
                CORRECT_LOGIN.equals(login) && CORRECT_PASSWORD.equals(password)
        ).delay(500, TimeUnit.MILLISECONDS);
    }
}
