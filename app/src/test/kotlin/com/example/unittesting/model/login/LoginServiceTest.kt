package com.example.unittesting.model.login

import com.example.unittesting.model.login.LoginService.CORRECT_LOGIN
import com.example.unittesting.model.login.LoginService.CORRECT_PASSWORD
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LoginServiceTest {

    val objectUnderTest = LoginService()

    @Test
    fun loginWithCorrectLoginAndPassword() {
        //given
        val login = CORRECT_LOGIN
        val password = CORRECT_PASSWORD
        //when
        val result = objectUnderTest.login(login, password)
        //then
        assertThat(result).isTrue()
    }

    @Test
    fun doNotLoginWithCorrectLoginOnly() {
        //given
        val login = CORRECT_LOGIN
        val password = "anyPassword"
        //when
        val result = objectUnderTest.login(login, password)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun doNotLoginWithCorrectPasswordOnly() {
        //given
        val login = "anyLogin"
        val password = CORRECT_PASSWORD
        //when
        val result = objectUnderTest.login(login, password)
        //then
        assertThat(result).isFalse()
    }
}