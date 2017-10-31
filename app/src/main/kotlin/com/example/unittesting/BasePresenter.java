package com.example.unittesting;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<T> implements Presenter<T> {

    T view;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void createView(T view) {
        this.view = view;
    }

    @Override
    public void destroyView() {
        compositeDisposable.clear();
        view = null;
    }

    protected void bindToLifecycle(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected T getView() {
        return view;
    }
}
