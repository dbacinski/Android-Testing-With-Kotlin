package com.example.unittesting.login.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.unittesting.R;
import com.example.unittesting.ResourceProvider;
import com.example.unittesting.SchedulersFactory;
import com.example.unittesting.login.model.LoginCredentials;
import com.example.unittesting.login.model.LoginRepository;
import com.example.unittesting.login.model.LoginUseCase;
import com.example.unittesting.login.model.LoginValidator;
import com.example.unittesting.login.presenter.LoginPresenter;
import com.example.unittesting.login.presenter.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Based on Google Login Screen example
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.email)
    EditText loginView;
    @BindView(R.id.password)
    EditText passwordView;
    @BindView(R.id.login_progress)
    View progressView;
    @BindView(R.id.login_form)
    View loginFormView;

    //TODO @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        passwordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login) {
                onSignInClick();
                return true;
            }
            return false;
        });

        loginPresenter = new LoginPresenter(new ResourceProvider(getResources()), new LoginValidator(), new LoginUseCase(new LoginRepository()), new SchedulersFactory());

        loginPresenter.createView(this);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.destroyView();
        super.onDestroy();
    }

    @OnClick(R.id.email_sign_in_button)
    public void onSignInClick() {
        loginPresenter.attemptLogin(new LoginCredentials()
                .withLogin(loginView.getText().toString())
                .withPassword(passwordView.getText().toString()));
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void hideProgress() {
        showProgress(false);
    }

    @Override
    public void onLoginSuccessful() {
        finish();
    }

    @Override
    public void showLoginError(String errorMessage) {
        loginView.setError(errorMessage);
    }

    @Override
    public void showPasswordError(String errorMessage) {
        passwordView.setError(errorMessage);
    }

    @Override
    public void requestLoginFocus() {
        loginView.requestFocus();
    }

    @Override
    public void requestPasswordFocus() {
        passwordView.requestFocus();
    }

    void showProgress(boolean progressVisible) {
        int animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginFormView.setVisibility(progressVisible ? View.GONE : View.VISIBLE);
        loginFormView.animate().setDuration(animationDuration).alpha(progressVisible ? 0 : 1);

        progressView.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(animationDuration).alpha(progressVisible ? 1 : 0);
    }
}

