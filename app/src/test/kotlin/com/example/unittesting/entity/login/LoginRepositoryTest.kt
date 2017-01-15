package com.example.unittesting.entity.login

import com.example.unittesting.entity.login.LoginRepository.CORRECT_LOGIN
import com.example.unittesting.entity.login.LoginRepository.CORRECT_PASSWORD
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(result).isTrue()
    }

    @Test
    fun `do not login with only correct login`() {
        //given
        val login = CORRECT_LOGIN
        val password = "anyPassword"
        //when
        val result = objectUnderTest.login(login, password)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `do not login with only correct password`() {
        //given
        val login = "anyLogin"
        val password = CORRECT_PASSWORD
        //when
        val result = objectUnderTest.login(login, password)
        //then
        assertThat(result).isFalse()
    }
}