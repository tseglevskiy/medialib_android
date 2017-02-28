package ru.roscha_akademii.medialib.book.onebook.presenter

import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.*
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile
import ru.roscha_akademii.medialib.book.onebook.view.OneBookView
import ru.roscha_akademii.medialib.book.onebook.view.`OneBookView$$State`
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.stub.StorageStub

class OneBookPresenterTest {
    @Mock
    lateinit var bookDb: BookDb

    lateinit var storage: Storage

    @Mock
    lateinit var view: OneBookView

    @Mock
    lateinit var viewState: `OneBookView$$State`

    @Captor
    lateinit var stringCaptor: ArgumentCaptor<String>

    @Captor
    lateinit var filesCaptor: ArgumentCaptor<List<BookFile>>

    lateinit var presenter: OneBookPresenter // SUT

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        storage = StorageStub()
        whenever(bookDb.getBook(any())).thenReturn(book1)
        whenever(bookDb.getBookFile(any())).thenReturn(files1)

        presenter = OneBookPresenter(bookDb, storage) // SUT
    }

    @Test
    fun attachView_nothingHappens() {
        presenter.attachView(view)
        presenter.setViewState(viewState)

        Mockito.verifyNoMoreInteractions(view)
        Mockito.verifyNoMoreInteractions(viewState)
    }

    @Test
    fun start_AsksBookDb() {
        presenter.attachView(view)
        presenter.setViewState(viewState)
        presenter.start(book1.id)

        verify(bookDb, times(1)).getBook(book1.id)
    }

    @Test
    fun start_showTitie() {
        presenter.attachView(view)
        presenter.setViewState(viewState)
        presenter.start(book1.id)

        verify(viewState, times(1)).showTitle(capture(stringCaptor))
        assertEquals(book1.title, stringCaptor.allValues[0])
    }

    @Test
    fun start_showDescription() {
        presenter.attachView(view)
        presenter.setViewState(viewState)
        presenter.start(book1.id)

        verify(viewState, times(1)).showDescription(capture(stringCaptor))
        assertEquals(book1.description, stringCaptor.allValues[0])
    }

    @Test
    fun start_getBookFiles() {
        presenter.attachView(view)
        presenter.setViewState(viewState)
        presenter.start(book1.id)

        verify(bookDb, times(1)).getBookFile(book1.id)
    }

    @Test
    fun start_showFilesList() {
        presenter.attachView(view)
        presenter.setViewState(viewState)
        presenter.start(book1.id)

        verify(viewState, times(1)).showFiles(capture(filesCaptor))
        val files = filesCaptor.allValues[0]
        assertEquals(files1.size, files.size)
        assertEquals(files1[0], files[0])
    }

    companion object {
        val book1 = Book(
                id = 345L,
                title = "book title",
                description = "book descr",
                picture = "http://1"

        )

        val files1 = arrayOf(BookFile(book1, "http://2")).toList()
    }
}