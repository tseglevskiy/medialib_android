package ru.roscha_akademii.medialib.book.model.remote

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile
import ru.roscha_akademii.medialib.book.model.remote.entity.BookAnswer
import ru.roscha_akademii.medialib.book.model.remote.entity.BookDTO
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.update.UpdateCallback

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class BookUpdateTest {
    val bookDto1 = BookDTO(
            id = 1111,
            title = "title one",
            picture = "picture url one",
            description = "description one",
            files = arrayOf("http://1", "http://2").toList()
    )

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
    lateinit var bookCaptor: ArgumentCaptor<Book>

    @Captor
    lateinit var bookFileCaptor: ArgumentCaptor<BookFile>

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
    fun runBookList() {
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
    fun successResultWithData_saveBook() {
        // 1. если вызывается update()...
        updater.update(callback)

        // ...то в процессе работы будет один запрос к серверу
        Mockito.verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        // 2. если сервер вернет ответ, содержащий один bookDto1...
        val answer = BookAnswer()
        answer.list = arrayOf(bookDto1).toList()

        assertEquals(1, answer.list!!.size)

        val response = Response.success(answer)

        requestCallback.onResponse(call, response)

        // ...то будет вызыван saveBook для этой книги
        Mockito.verify(bookDb, times(1)).saveBook(capture(bookCaptor))

        val book = bookCaptor.allValues[0]
        assertEquals(bookDto1.id, book.id)
        assertEquals(bookDto1.title, book.title)
        assertEquals(bookDto1.description, book.description)
        assertEquals(bookDto1.picture, book.picture)
    }

    @Test
    fun successResultWithData_saveFiles() {
        // 1. если вызывается update()...
        updater.update(callback)

        // ...то в процессе работы будет один запрос к серверу
        Mockito.verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        // 2. если сервер вернет ответ, содержащий один bookDto1...
        val answer = BookAnswer()
        answer.list = arrayOf(bookDto1).toList()

        assertEquals(1, answer.list!!.size)

        val response = Response.success(answer)

        requestCallback.onResponse(call, response)

        // ...то будет вызыван saveBookFile для обоих файлов этой книги
        Mockito.verify(bookDb, times(2)).saveBookFile(capture(bookFileCaptor))

        val files = bookFileCaptor
                .allValues
                .toList()
                .sortedBy(BookFile::url)

        // первый
        assertEquals(bookDto1.id, files.get(0).bookId)
        assertEquals(bookDto1.files!![0], files.get(0).url)
        // второй
        assertEquals(bookDto1.id, files.get(1).bookId)
        assertEquals(bookDto1.files!![1], files.get(1).url)
    }

    @Test
    fun successResultWithData_success() {
        // 1. если вызывается update()...
        updater.update(callback)

        // ...то в процессе работы будет один запрос к серверу
        Mockito.verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        // 2. если сервер вернет ответ, содержащий один bookDto1...
        val answer = BookAnswer()
        answer.list = arrayOf(bookDto1).toList()
        val response = Response.success(answer)

        requestCallback.onResponse(call, response)

        // ...то будет вызван callback.onSuccess()

        Mockito.verify(callback).onSuccess()
    }
}