package com.example.unittesting.presenter.login;

import android.os.AsyncTask;

import com.example.unittesting.R;
import com.example.unittesting.model.ResourceProvider;
import com.example.unittesting.model.login.LoginCredentials;
import com.example.unittesting.model.login.LoginService;
import com.example.unittesting.model.login.LoginUseCase;
import com.example.unittesting.model.login.LoginValidator;
import com.example.unittesting.presenter.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginView> {

    ResourceProvider resourceProvider;
    LoginValidator loginValidator;

    public LoginPresenter(ResourceProvider resourceProvider, LoginValidator loginValidator) {
        this.resourceProvider = resourceProvider;
        this.loginValidator = loginValidator;
    }

    public void attemptLogin(LoginCredentials loginCredentials) {

        boolean validationError = validatePassword(loginCredentials, false);

        validationError = validateLogin(loginCredentials, validationError);

        if (validationError) {
            return;
        }

        getView().showProgress();

        new UserLoginTask(loginCredentials).execute((Void) null);

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

    //TODO convert to Rx
    class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        LoginUseCase loginUseCase = new LoginUseCase(new LoginService()); //TODO @Inject
        private final LoginCredentials loginCredentials;

        UserLoginTask(LoginCredentials loginCredentials) {
            this.loginCredentials = loginCredentials;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }

            return loginUseCase.loginWithCredentialsWithStatus(loginCredentials);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getView().hideProgress();

            if (success) {
                getView().onLoginSuccessful();
            } else {
                getView().showPasswordError(resourceProvider.getString(R.string.error_incorrect_password));
                getView().requestPasswordFocus();
            }
        }

        @Override
        protected void onCancelled() {
            getView().hideProgress();
        }
    }
}
