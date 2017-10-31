package com.example.unittesting;

interface Presenter<T> {

    void createView(T view);

    void destroyView();
}
