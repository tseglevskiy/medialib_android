package ru.roscha_akademii.medialib.book.showlist.item.presenter

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
import ru.roscha_akademii.medialib.book.showlist.item.view.BookItemView
import ru.roscha_akademii.medialib.book.showlist.item.view.`BookItemView$$State`
import ru.roscha_akademii.medialib.storage.Storage

class BookItemPresenterTest {
    lateinit var presenter: BookItemPresenter // SUT

    @Mock
    lateinit var view: BookItemView

    @Mock
    lateinit var viewState: `BookItemView$$State`

    @Mock
    lateinit var bookDb: BookDb

    @Mock
    lateinit var storage: Storage

    @Captor
    lateinit var longCaptor: ArgumentCaptor<Long>

    @Captor
    lateinit var stringCaptor: ArgumentCaptor<String>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = BookItemPresenter(bookDb, storage) // SUT

        presenter.setViewState(viewState)
        presenter.attachView(view)
    }

    @Test
    fun showId() {
        whenever(bookDb.getBook(book1.id)).thenReturn(book1)

        presenter.bookId = book1.id

        verify(viewState).showId(capture(longCaptor))

        assertEquals(book1.id, longCaptor.allValues[0])
    }

    @Test
    fun requestToDb() {
        whenever(bookDb.getBook(book1.id)).thenReturn(book1)

        presenter.bookId = book1.id

        verify(bookDb).getBook(capture(longCaptor))

        assertEquals(book1.id, longCaptor.allValues[0])
    }

    @Test
    fun showTitle() {
        val id = 1111L
        whenever(bookDb.getBook(id)).thenReturn(book1)

        presenter.bookId = id
        verify(viewState).showTitle(capture(stringCaptor))
    }

    @Test
    fun showPicture() {
        val id = 1111L
        val url = "url_from_storage"
        whenever(bookDb.getBook(id)).thenReturn(book1)
        whenever(storage.getLocalUriIfAny(any())).thenReturn(url)

        presenter.bookId = id

        verify(storage).getLocalUriIfAny(capture(stringCaptor))
        assertEquals(book1.picture, stringCaptor.allValues[0])

        verify(viewState).showImage(capture(stringCaptor))
        assertEquals(url, stringCaptor.allValues[1])
    }

    companion object {

        /*
        test data
         */
        private val book1 = Book(
                1111,
                "title one",
                "picture url one",
                "description one",
                null)

    }
}