package ru.roscha_akademii.medialib.book.showlist.list.view

import com.arellomobile.mvp.MvpView
import ru.roscha_akademii.medialib.book.model.local.entity.Book

interface BookListView : MvpView{
    fun showBooks(list: List<Book>)
}
