package com.example.unittesting.entity.login;

import timber.log.Timber;

public class LoginRepository {

    static final String CORRECT_LOGIN = "dbacinski";
    static final String CORRECT_PASSWORD = "correct";

    public boolean login(String login, String password) {
        Timber.v("login %s with password %s", login, password);
        return CORRECT_LOGIN.equals(login) && CORRECT_PASSWORD.equals(password);
    }
}
