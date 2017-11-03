package com.example.unittesting.login.presenter

import android.content.res.Resources
import com.example.unittesting.ResourceProvider
import com.example.unittesting.SchedulersFactory
import com.example.unittesting.login.model.LoginCredentials
import com.example.unittesting.login.model.LoginRepository
import com.example.unittesting.login.model.LoginUseCase
import com.example.unittesting.login.model.LoginValidator
import com.nhaarman.mockito_kotlin.any
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*

class LoginPresenterTest {

    val loginViewMock: LoginView = mock(LoginView::class.java)
    val resourcesStub: Resources = mock(Resources::class.java)
    val schedulersFactoryStub: SchedulersFactory = mock(SchedulersFactory::class.java)
    val loginRepositoryStub = mock(LoginRepository::class.java)

    val objectUnderTest = LoginPresenter(ResourceProvider(resourcesStub), LoginValidator(), LoginUseCase(loginRepositoryStub), schedulersFactoryStub)

    @Before
    fun setUp() {
        removeObserveOnMainThreadScheduler()
        objectUnderTest.createView(loginViewMock)
    }

    @Test
    fun `login with correct data`() {
        //given
        given(loginRepositoryStub.login(any(), any())).willReturn(Observable.just(true))
        //when
        objectUnderTest.attemptLogin(LoginCredentials(login = "correct", password = "correct"))
        //then
        verify(loginViewMock).onLoginSuccessful()
    }

    @Test
    fun `login with correct data with progress indication`() {
        //given
        given(loginRepositoryStub.login(any(), any())).willReturn(Observable.just(true))
        //when
        objectUnderTest.attemptLogin(LoginCredentials(login = "correct", password = "correct"))
        //then
        val ordered = inOrder(loginViewMock)
        ordered.verify(loginViewMock).showProgress()
        ordered.verify(loginViewMock).hideProgress()
    }

    @Test
    fun `login with valid but incorrect data`() {
        //given
        given(resourcesStub.getString(anyInt())).willReturn("error")
        given(loginRepositoryStub.login(any(), any())).willReturn(Observable.just(false))
        //when
        objectUnderTest.attemptLogin(LoginCredentials(login = "valid", password = "incorrectPassword"))
        //then
        val ordered = inOrder(loginViewMock)
        ordered.verify(loginViewMock).showLoginError(null)
        ordered.verify(loginViewMock).showPasswordError("error")
    }

    @Test
    fun `show validation error for empty email`() {
        //given
        given(resourcesStub.getString(anyInt())).willReturn("error")
        val login = ""
        //when
        objectUnderTest.attemptLogin(LoginCredentials(login = login, password = "validPassword"))
        //then
        verify(loginViewMock).showLoginError("error")
        verify(loginViewMock).showPasswordError(null)
    }

    @Test
    fun `show validation error for empty email and too short password`() {
        //given
        given(resourcesStub.getString(anyInt())).willReturn("error")
        val login = ""
        val password = "short"
        //when
        objectUnderTest.attemptLogin(LoginCredentials(login = login, password = password))
        //then
        verify(loginViewMock).showLoginError("error")
        verify(loginViewMock).showPasswordError("error")
    }

    @Test
    fun `show validation error for too short password`() {
        //given
        given(resourcesStub.getString(anyInt())).willReturn("error")
        val password = "short"
        //when
        objectUnderTest.attemptLogin(LoginCredentials(login = "valid", password = password))
        //then
        verify(loginViewMock).showLoginError(null)
        verify(loginViewMock).showPasswordError("error")
    }

    private fun removeObserveOnMainThreadScheduler() {
        given(schedulersFactoryStub.createMainThreadSchedulerTransformer<Boolean>()).willReturn(ObservableTransformer { it })
    }
}
