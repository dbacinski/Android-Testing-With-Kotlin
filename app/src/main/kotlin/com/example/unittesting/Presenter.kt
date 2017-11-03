package com.example.unittesting

internal interface Presenter<in T> {

    fun createView(view: T)

    fun destroyView()
}
