package com.example.unittesting

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<T> : Presenter<T> {

    var view: T? = null
    private val compositeDisposable = CompositeDisposable()

    override fun createView(view: T) {
        this.view = view
    }

    fun bindToLifecycle(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun destroyView() {
        compositeDisposable.clear()
        view = null
    }
}
