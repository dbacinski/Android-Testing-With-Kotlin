package com.example.unittesting.login.model

import io.reactivex.Observable
import io.reactivex.Observable.fromCallable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LoginRepository {

    fun login(login: String, password: String): Observable<Boolean> {
        Timber.v("login %s with password %s", login, password)

        return fromCallable { CORRECT_LOGIN == login && CORRECT_PASSWORD == password }
                .delay(2000, TimeUnit.MILLISECONDS)
    }

    companion object {
        internal val CORRECT_LOGIN = "dbacinski"
        internal val CORRECT_PASSWORD = "correct"
    }
}
