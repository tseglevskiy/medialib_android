package ru.roscha_akademii.medialib.book.onebook.view

import com.arellomobile.mvp.MvpView
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile

interface OneBookView : MvpView {
    fun showTitle(title: String?)
    fun showDescription(description: String?)
    fun showFiles(files: List<BookFile>)
}

