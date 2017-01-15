package com.example.unittesting.presenter.login

import android.content.res.Resources
import com.example.unittesting.domain.ResourceProvider
import com.example.unittesting.domain.SchedulersFactory
import com.example.unittesting.domain.login.LoginUseCase
import com.example.unittesting.entity.login.LoginCredentials
import com.example.unittesting.entity.login.LoginRepository
import com.example.unittesting.entity.login.LoginValidator
import io.reactivex.ObservableTransformer
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.*

class LoginPresenterTest {

    val loginViewMock: LoginView = Mockito.mock(LoginView::class.java)
    val resourcesMock: Resources = Mockito.mock(Resources::class.java)
    val schedulersFactoryMock: SchedulersFactory = Mockito.mock(SchedulersFactory::class.java)

    val objectUnderTest = LoginPresenter(ResourceProvider(resourcesMock), LoginValidator(), LoginUseCase(LoginRepository()), schedulersFactoryMock)

    @Before
    fun setUp() {
        removeObserveOnMainThreadScheduler()
    }

    @Test
    fun `login with correct data`() {
        //given
        objectUnderTest.createView(loginViewMock)
        val login = "dbacinski"
        val password = "correct"
        //when
        objectUnderTest.attemptLogin(LoginCredentials().withLogin(login).withPassword(password))
        //then
        verify(loginViewMock, timeout(5000)).onLoginSuccessful()
    }

    @Test
    fun `login with correct data with progress indication`() {
        //given
        objectUnderTest.createView(loginViewMock)
        val login = "any"
        val password = "anyValidPassword"
        //when
        objectUnderTest.attemptLogin(LoginCredentials().withLogin(login).withPassword(password))
        //then
        val ordered = inOrder(loginViewMock)
        ordered.verify(loginViewMock).showProgress()
        ordered.verify(loginViewMock, timeout(5000)).hideProgress()
    }

    @Test
    fun `show validation error for incorrect data`() {
        //given
        objectUnderTest.createView(loginViewMock)
        given(resourcesMock.getString(anyInt())).willReturn("error")
        val login = "dbacinski"
        val password = "incorrect"
        //when
        objectUnderTest.attemptLogin(LoginCredentials().withLogin(login).withPassword(password))
        //then
        val ordered = inOrder(loginViewMock)
        ordered.verify(loginViewMock).showPasswordError(null)
        ordered.verify(loginViewMock, timeout(5000)).showPasswordError("error")
    }

    @Test
    fun `show validation error for empty email`() {
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
    fun `show validation error for empty email and password`() {
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
    fun `show validation error for empty password`() {
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

    private fun removeObserveOnMainThreadScheduler() {
        given(schedulersFactoryMock.createMainThreadSchedulerTransformer<Boolean>()).willReturn(ObservableTransformer { it })
    }
}
