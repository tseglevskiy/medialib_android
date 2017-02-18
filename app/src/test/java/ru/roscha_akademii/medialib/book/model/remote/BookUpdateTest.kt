package ru.roscha_akademii.medialib.book.model.remote

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
import ru.roscha_akademii.medialib.book.model.remote.entity.BookAnswer
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.update.UpdateCallback
import java.util.*

class BookUpdateTest {
    val book1 = Book(
            id = 1111,
            title = "title one",
            picture = "picture url one",
            description = "description one")

    @Mock
    lateinit var bookApi: BookApi

    @Mock
    lateinit var storage: Storage

    @Mock
    lateinit var call: Call<BookAnswer>

    @Mock
    lateinit var callback: UpdateCallback

    @Captor
    lateinit var requestCallCaptor: ArgumentCaptor<Callback<BookAnswer>>

    @Captor
    lateinit var bookListCaptor: ArgumentCaptor<ArrayList<Book>>

    @Mock
    lateinit var bookDb: BookDb

    lateinit var updater: BookUpdate // SUT

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(bookApi.bookList()).thenReturn(call)

        updater = BookUpdate(bookApi, bookDb, storage) // SUT
    }

    @Test
    fun runVideoList() {
        updater.update(callback)

        Mockito.verify(bookApi).bookList()
    }

    @Test
    fun failedResult() {
        updater.update(callback)

        Mockito.verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        requestCallback.onFailure(call, RuntimeException("hello"))

        Mockito.verify(callback).onFail()
    }

    @Test
    fun successResultWithEmptyList() {
        updater.update(callback)

        Mockito.verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        val answer = BookAnswer()
        val response = Response.success(answer)
        requestCallback.onResponse(call, response)

        Mockito.verify(bookDb, times(0)).saveBooks(any())
        Mockito.verify(callback).onSuccess()
    }

    @Test
    fun successResultWithData() {
        updater.update(callback)

        Mockito.verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        val list = ArrayList<Book>()
        list.add(book1)
        val answer = BookAnswer()
        answer.list = list
        val response = Response.success(answer)
        requestCallback.onResponse(call, response)

        Mockito.verify(bookDb, times(1)).saveBooks(capture(bookListCaptor))
        assertEquals(list, bookListCaptor.allValues[0])

        Mockito.verify(callback).onSuccess()
    }
}