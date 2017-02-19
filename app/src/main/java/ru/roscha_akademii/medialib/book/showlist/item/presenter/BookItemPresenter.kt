package ru.roscha_akademii.medialib.book.showlist.item.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.showlist.item.view.BookItemView
import ru.roscha_akademii.medialib.storage.Storage

@InjectViewState
class BookItemPresenter(val bookDb: BookDb,
                        val storage: Storage) : MvpPresenter<BookItemView>() {
    var bookId: Long? = null
        set(value) {
            field = value
            updateView()
        }

    fun updateView() {
        bookId?.let {
            val book = bookDb.getBook(it)

            with(book) {
                viewState.showId(it)
                viewState.showTitle(title)
                viewState.showImage(picture?.let { storage.getLocalUriIfAny(it) })
            }
        }
    }
}