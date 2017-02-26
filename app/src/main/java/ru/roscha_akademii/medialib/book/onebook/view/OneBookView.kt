package ru.roscha_akademii.medialib.book.onebook.view

import com.arellomobile.mvp.MvpView

interface OneBookView : MvpView {
    fun showTitle(title: String?)
    fun showDescription(description: String?)
}

