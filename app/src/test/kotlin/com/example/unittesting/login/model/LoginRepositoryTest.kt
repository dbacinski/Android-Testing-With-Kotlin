package com.example.unittesting.login.model

import com.example.unittesting.login.model.LoginRepository.Companion.CORRECT_LOGIN
import com.example.unittesting.login.model.LoginRepository.Companion.CORRECT_PASSWORD
import org.junit.Test

class LoginRepositoryTest {

    val objectUnderTest = LoginRepository()

    @Test
    fun `login with correct login and password`() {
        //given
        val login = CORRECT_LOGIN
        val password = CORRECT_PASSWORD
        //when
        val result = objectUnderTest.login(login, password)
        //then
        result.test().await()
                .assertResult(true)
    }

    @Test
    fun `do not login with only correct login`() {
        //given
        val login = CORRECT_LOGIN
        val password = "anyPassword"
        //when
        val result = objectUnderTest.login(login, password)
        //then
        result.test().await()
                .assertResult(false)
    }

    @Test
    fun `do not login with only correct password`() {
        //given
        val login = "anyLogin"
        val password = CORRECT_PASSWORD
        //when
        val result = objectUnderTest.login(login, password)
        //then
        result.test().await()
                .assertResult(false)
    }
}