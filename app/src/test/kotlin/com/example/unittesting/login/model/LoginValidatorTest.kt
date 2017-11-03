package com.example.unittesting.login.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LoginValidatorTest {

    val objectUnderTest = LoginValidator()

    @Test
    fun `empty login is invalid`() {
        //when
        val result = objectUnderTest.validateLogin("")
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `not empty login is valid`() {
        //when
        val result = objectUnderTest.validateLogin("anyLogin")
        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `empty password is invalid`() {
        //when
        val result = objectUnderTest.validatePassword("")
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `password is invalid if shorter then limit`() {
        //when
        val result = objectUnderTest.validatePassword("12345")
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `password is valid if equal to limit`() {
        //when
        val result = objectUnderTest.validatePassword("123456")
        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `password is valid if longer than limit`() {
        //when
        val result = objectUnderTest.validatePassword("1234567")
        //then
        assertThat(result).isTrue()
    }
}