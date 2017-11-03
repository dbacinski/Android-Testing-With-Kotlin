package com.example.unittesting.login.model

class LoginValidator {

    companion object {
        val EMPTY = ""
        val MIN_PASSWORD_LENGTH = 6
    }

    fun validateLogin(login: String): Boolean {
        return login != EMPTY
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
    }
}
