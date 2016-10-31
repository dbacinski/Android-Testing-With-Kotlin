package com.example.unittesting.presenter.login;

import com.example.unittesting.R;
import com.example.unittesting.model.ResourceProvider;
import com.example.unittesting.model.login.LoginCredentials;
import com.example.unittesting.model.login.LoginUseCase;
import com.example.unittesting.model.login.LoginValidator;
import com.example.unittesting.presenter.BasePresenter;
import com.example.unittesting.presenter.SchedulersFactory;

import io.reactivex.functions.Consumer;

public class LoginPresenter extends BasePresenter<LoginView> {

    ResourceProvider resourceProvider;
    LoginValidator loginValidator;
    LoginUseCase loginUseCase;
    SchedulersFactory schedulersFactory;

    public LoginPresenter(ResourceProvider resourceProvider, LoginValidator loginValidator, LoginUseCase loginUseCase, SchedulersFactory schedulersFactory) {
        this.resourceProvider = resourceProvider;
        this.loginValidator = loginValidator;
        this.loginUseCase = loginUseCase;
        this.schedulersFactory = schedulersFactory;
    }

    public void attemptLogin(LoginCredentials loginCredentials) {

        boolean validationError = validatePassword(loginCredentials, false);

        validationError = validateLogin(loginCredentials, validationError);

        if (validationError) {
            return;
        }

        getView().showProgress();

        loginUseCase.loginWithCredentialsWithStatus(loginCredentials)
                .compose(schedulersFactory.<Boolean>createMainThreadSchedulerTransformer())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        getView().hideProgress();

                        if (result) {
                            getView().onLoginSuccessful();
                        } else {
                            getView().showPasswordError(resourceProvider.getString(R.string.error_incorrect_password));
                            getView().requestPasswordFocus();
                        }
                    }
                });
    }

    private boolean validatePassword(LoginCredentials loginCredentials, boolean validationError) {
        if (!loginValidator.validatePassword(loginCredentials.password)) {
            getView().showPasswordError(resourceProvider.getString(R.string.error_invalid_password));
            getView().requestPasswordFocus();
            validationError = true;
        } else {
            getView().showPasswordError(null);
        }
        return validationError;
    }

    private boolean validateLogin(LoginCredentials loginCredentials, boolean validationError) {
        if (!loginValidator.validateLogin(loginCredentials.login)) {
            getView().showLoginError(resourceProvider.getString(R.string.error_field_required));
            getView().requestLoginFocus();
            validationError = true;
        } else {
            getView().showLoginError(null);
        }
        return validationError;
    }
}
