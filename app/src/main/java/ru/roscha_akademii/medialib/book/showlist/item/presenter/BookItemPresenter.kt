package ru.roscha_akademii.medialib.book.showlist.item.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.book.showlist.item.view.BookItemView

@InjectViewState
class BookItemPresenter : MvpPresenter<BookItemView>() {
    var bookId: Long? = null
        set(value) {
            field = value
            updateView()
        }

    fun updateView() {
        bookId?.let {
            viewState.showId(it)
        }
    }
}