package com.example.unittesting.entity.login

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.Test

class LoginValidatorTest {

    val objectUnderTest = LoginValidator()

    @Test
    fun emptyLoginIsInvalid() {
        //when
        val result = objectUnderTest.validateLogin("")
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun notEmptyLoginIsValid() {
        //when
        val result = objectUnderTest.validateLogin("anyLogin")
        //then
        assertThat(result).isTrue()
    }

    @Test
    fun throwsExceptionForNullLogin() {
        //when
        val catchThrowable = catchThrowable { objectUnderTest.validateLogin(null) }
        //then
        assertThat(catchThrowable).isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun emptyPasswordIsInvalid() {
        //when
        val result = objectUnderTest.validatePassword("")
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun throwsExceptionForNullPassword() {
        //when
        val catchThrowable = catchThrowable { objectUnderTest.validatePassword(null) }
        //then
        assertThat(catchThrowable).isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun passwordIsInvalidIfShorterThenLimit() {
        //when
        val result = objectUnderTest.validatePassword("12345")
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun passwordIsValidIfEqualToLimit() {
        //when
        val result = objectUnderTest.validatePassword("123456")
        //then
        assertThat(result).isTrue()
    }

    @Test
    fun passwordIsValidIfLongerThanLimit() {
        //when
        val result = objectUnderTest.validatePassword("1234567")
        //then
        assertThat(result).isTrue()
    }
}