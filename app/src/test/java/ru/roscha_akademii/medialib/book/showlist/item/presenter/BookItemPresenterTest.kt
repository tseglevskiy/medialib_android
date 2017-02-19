package ru.roscha_akademii.medialib.book.showlist.item.presenter

import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.roscha_akademii.medialib.book.showlist.item.view.BookItemView
import ru.roscha_akademii.medialib.book.showlist.item.view.`BookItemView$$State`

class BookItemPresenterTest {
    lateinit var presenter: BookItemPresenter // SUT

    @Mock
    lateinit var view: BookItemView

    @Mock
    lateinit var viewState: `BookItemView$$State`

    @Captor
    lateinit var longCaptor: ArgumentCaptor<Long>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = BookItemPresenter() // SUT

        presenter.setViewState(viewState)
        presenter.attachView(view)
    }

    @Test
    fun showId() {
        val id = 222L

        presenter.bookId = id

        verify(viewState).showId(capture(longCaptor))

        assertEquals(id, longCaptor.allValues[0])
    }
}