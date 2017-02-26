package ru.roscha_akademii.medialib.book.onebook.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.onebook.view.OneBookView
import ru.roscha_akademii.medialib.storage.Storage

@InjectViewState
class OneBookPresenter(val bookDb: BookDb, val storage: Storage) : MvpPresenter<OneBookView>() {
    lateinit var book: Book

    fun start(videoId: Long) {
        book = bookDb.getBook(videoId)

        with(book) {
            viewState.showTitle(title)
            viewState.showDescription(description)
        }
    }

}
