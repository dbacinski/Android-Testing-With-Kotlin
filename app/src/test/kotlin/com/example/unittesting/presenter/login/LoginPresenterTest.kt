package com.example.unittesting.presenter.login

import android.content.res.Resources
import com.example.unittesting.model.ResourceProvider
import com.example.unittesting.model.login.LoginCredentials
import com.example.unittesting.model.login.LoginValidator
import org.junit.Ignore
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.verify

class LoginPresenterTest {

    val loginViewMock: LoginView = Mockito.mock(LoginView::class.java)
    val resourcesMock: Resources = Mockito.mock(Resources::class.java)

    val objectUnderTest = LoginPresenter(ResourceProvider(resourcesMock), LoginValidator())

    @Ignore("remove AsyncTask first")
    @Test
    fun loginWithCorrectData() {
        //given
        objectUnderTest.createView(loginViewMock)
        val login = "dbacinski"
        val password = "correct"
        //when
        objectUnderTest.attemptLogin(LoginCredentials().withLogin(login).withPassword(password))
        //then
        verify(loginViewMock).showProgress()
        verify(loginViewMock).onLoginSuccessful()
    }

    @Test
    fun showValidationErrorForEmptyEmail() {
        //given
        objectUnderTest.createView(loginViewMock)
        given(resourcesMock.getString(anyInt())).willReturn("error")
        val login = ""
        val password = "validPassword"
        //when
        objectUnderTest.attemptLogin(LoginCredentials().withLogin(login).withPassword(password))
        //then
        verify(loginViewMock).showLoginError("error")
        verify(loginViewMock).showPasswordError(null)
    }

    @Test
    fun showValidationErrorForEmptyEmailAndPassword() {
        //given
        objectUnderTest.createView(loginViewMock)
        given(resourcesMock.getString(anyInt())).willReturn("error")
        val login = ""
        val password = ""
        //when
        objectUnderTest.attemptLogin(LoginCredentials().withLogin(login).withPassword(password))
        //then
        verify(loginViewMock).showLoginError("error")
        verify(loginViewMock).showPasswordError("error")
    }

    @Test
    fun showValidationErrorForEmptyPassword() {
        //given
        objectUnderTest.createView(loginViewMock)
        given(resourcesMock.getString(anyInt())).willReturn("error")
        val login = "dbacinski"
        val password = ""
        //when
        objectUnderTest.attemptLogin(LoginCredentials().withLogin(login).withPassword(password))
        //then
        verify(loginViewMock).showLoginError(null)
        verify(loginViewMock).showPasswordError("error")
    }
}