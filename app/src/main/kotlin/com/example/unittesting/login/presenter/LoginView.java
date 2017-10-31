package com.example.unittesting.login.presenter;

public interface LoginView {

    void showProgress();

    void hideProgress();

    void onLoginSuccessful();

    void showLoginError(String errorMessage);

    void showPasswordError(String errorMessage);

    void requestLoginFocus();

    void requestPasswordFocus();
}
