package com.example.unittesting.presenter.login;

public interface LoginView {

    void showProgress();

    void hideProgress();

    void onLoginSuccessful();

    void showLoginError(String errorMessage);

    void showPasswordError(String errorMessage);

    void requestLoginFocus();

    void requestPasswordFocus();
}
