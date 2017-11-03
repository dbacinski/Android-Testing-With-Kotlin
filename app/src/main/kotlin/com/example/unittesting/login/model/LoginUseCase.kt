package com.example.unittesting.login.model

import io.reactivex.Observable

class LoginUseCase(private val loginRepository: LoginRepository) {

    fun loginWithCredentialsWithStatus(credentials: LoginCredentials): Observable<Boolean> {
        return loginRepository.login(credentials.login, credentials.password)
    }
}
