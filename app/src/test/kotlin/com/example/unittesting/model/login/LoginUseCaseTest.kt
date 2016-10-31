package com.example.unittesting.model.login

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.Test
import org.mockito.Mockito

class LoginUseCaseTest {

    val objectUnderTest = LoginUseCase(Mockito.mock(LoginService::class.java))

    @Test
    fun throwsExceptionForNullLoginCredentials() {
        //given
        val loginCredentials = null
        //when
        val catchThrowable = catchThrowable { objectUnderTest.loginWithCredentialsWithStatus(loginCredentials) }
        //then
        assertThat(catchThrowable).isInstanceOf(NullPointerException::class.java)
    }
}