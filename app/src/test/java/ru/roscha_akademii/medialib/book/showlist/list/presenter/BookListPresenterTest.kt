package ru.roscha_akademii.medialib.book.showlist.list.presenter

import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.greenrobot.eventbus.EventBus
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.showlist.list.view.BookListView
import ru.roscha_akademii.medialib.book.showlist.list.view.`BookListView$$State`
import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import java.util.*


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class BookListPresenterTest {
    @Mock
    lateinit var bus: EventBus

    @Mock
    lateinit var bookDb: BookDb

    @Mock
    lateinit var navigator: ActivityNavigator

    @Mock
    lateinit var view: BookListView

    @Mock
    lateinit var viewState: `BookListView$$State`

    @Captor
    lateinit var listCaptor: ArgumentCaptor<List<Book>>

    @Captor
    lateinit var longCaptor: ArgumentCaptor<Long>

    lateinit var presenter: BookListPresenter // SUT

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = BookListPresenter(bus, bookDb, navigator) // SUT
        presenter.setViewState(viewState)
        presenter.attachView(view)
    }

    @Test
    fun registerBus() {
        verify(bus).register(presenter)
        verify(bus, times(0)).unregister(presenter)
    }

    @Test
    fun unregisterBus() {
        presenter.onDestroy()

        verify(bus).unregister(presenter)
    }

    @Test
    fun requestBoolList() {
        verify(bookDb).allBooks
    }

    @Test
    fun showBookList() {
        val list = ArrayList<Book>()

        whenever(bookDb.allBooks).thenReturn(list)

        verify(viewState).showBooks(capture(listCaptor))

        assertEquals(list, listCaptor.allValues[0])
    }

    @Test
    fun itemSelected() {
        val id = 555L

        presenter.wannaOpenBook(id)

        verify(navigator, times(1)).openBook(capture(longCaptor))
        assertEquals(id, longCaptor.allValues[0])
    }
}