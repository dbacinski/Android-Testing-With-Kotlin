package com.example.unittesting.login.presenter

interface LoginView {

    fun showProgress()

    fun hideProgress()

    fun onLoginSuccessful()

    fun showLoginError(errorMessage: String?)

    fun showPasswordError(errorMessage: String?)

    fun requestLoginFocus()

    fun requestPasswordFocus()
}
