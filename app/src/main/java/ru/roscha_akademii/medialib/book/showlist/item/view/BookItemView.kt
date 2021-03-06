package ru.roscha_akademii.medialib.book.showlist.item.view

import com.arellomobile.mvp.MvpView

interface BookItemView : MvpView {

    fun showId(id: Long)

    fun showTitle(title: String?)

    fun showImage(url: String?)
}
