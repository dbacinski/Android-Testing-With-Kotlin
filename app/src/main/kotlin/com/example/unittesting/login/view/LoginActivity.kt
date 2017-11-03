package com.example.unittesting.login.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.unittesting.R
import com.example.unittesting.ResourceProvider
import com.example.unittesting.SchedulersFactory
import com.example.unittesting.login.model.LoginCredentials
import com.example.unittesting.login.model.LoginRepository
import com.example.unittesting.login.model.LoginUseCase
import com.example.unittesting.login.model.LoginValidator
import com.example.unittesting.login.presenter.LoginPresenter
import com.example.unittesting.login.presenter.LoginView

/**
 * Based on Google Login Screen example
 */
class LoginActivity : AppCompatActivity(), LoginView {

    @BindView(R.id.email)
    lateinit var loginView: EditText
    @BindView(R.id.password)
    lateinit var passwordView: EditText
    @BindView(R.id.login_progress)
    lateinit var progressView: View
    @BindView(R.id.login_form)
    lateinit var loginFormView: View

    //TODO @Inject
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ButterKnife.bind(this)

        passwordView.setOnEditorActionListener { _, id, _ ->
            if (id == 1) {
                onSignInClick()
                true
            } else {
                false
            }
        }

        loginPresenter = LoginPresenter(ResourceProvider(resources), LoginValidator(), LoginUseCase(LoginRepository()), SchedulersFactory())
        loginPresenter.createView(this)
    }

    override fun onDestroy() {
        loginPresenter.destroyView()
        super.onDestroy()
    }

    @OnClick(R.id.email_sign_in_button)
    fun onSignInClick() {
        loginPresenter.attemptLogin(
                LoginCredentials(
                        login = loginView.text.toString(),
                        password = passwordView.text.toString()
                )
        )
    }

    override fun showProgress() {
        showProgress(true)
    }

    override fun hideProgress() {
        showProgress(false)
    }

    override fun onLoginSuccessful() {
        finish()
    }

    override fun showLoginError(errorMessage: String?) {
        loginView.error = errorMessage
    }

    override fun showPasswordError(errorMessage: String?) {
        passwordView.error = errorMessage
    }

    override fun requestLoginFocus() {
        loginView.requestFocus()
    }

    override fun requestPasswordFocus() {
        passwordView.requestFocus()
    }

    internal fun showProgress(progressVisible: Boolean) {
        val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        loginFormView.visibility = if (progressVisible) View.GONE else View.VISIBLE
        loginFormView.animate().setDuration(animationDuration.toLong()).alpha((if (progressVisible) 0 else 1).toFloat())

        progressView.visibility = if (progressVisible) View.VISIBLE else View.GONE
        progressView.animate().setDuration(animationDuration.toLong()).alpha((if (progressVisible) 1 else 0).toFloat())
    }
}

