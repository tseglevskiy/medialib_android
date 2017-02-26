package ru.roscha_akademii.medialib.book.showlist.list.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.showlist.list.view.BookListView
import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.update.event.DataDownloaded

@InjectViewState
class BookListPresenter(private val bus: EventBus,
                        private val bookDb: BookDb,
                        private val navigator: ActivityNavigator) : MvpPresenter<BookListView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getAndDisplayBookList()
        bus.register(this)
    }

//    override fun wannaOpenVideo(id: Long) {
//        navigator.openVideo(id)
//    }

    private fun getAndDisplayBookList() {
        Log.d("happy", "getAndDisplayBookList")
        val list = bookDb.allBooks
        viewState.showBooks(list)
    }

    fun wannaOpenBook(id: Long) {
        navigator.openBook(id)
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)
    }

    @Subscribe
    fun onEvent(event: DataDownloaded) {
        getAndDisplayBookList()
    }
}
