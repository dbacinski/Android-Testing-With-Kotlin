package com.example.unittesting.presenter;

interface Presenter<T> {

    void createView(T view);

    void destroyView();
}
