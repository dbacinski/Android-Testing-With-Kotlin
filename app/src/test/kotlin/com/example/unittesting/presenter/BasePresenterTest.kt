package com.example.unittesting.presenter

import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.concurrent.TimeUnit

class BasePresenterTest {

    val objectUnderTest = BasePresenter<TestView>()

    @Test
    fun disposeObservablesOnDestroyView() {
        //given
        val disposable = Observable.interval(100, TimeUnit.MILLISECONDS).subscribe()
        objectUnderTest.bindToLifecycle(disposable)
        //when
        objectUnderTest.destroyView()
        //then
        assertThat(disposable.isDisposed).isTrue()
    }

    @Test
    fun cleanViewReferenceOnDestroyView() {
        //given
        objectUnderTest.createView(TestView())
        //when
        objectUnderTest.destroyView()
        //then
        assertThat(objectUnderTest.getView()).isNull()
    }
}

class TestView {

}