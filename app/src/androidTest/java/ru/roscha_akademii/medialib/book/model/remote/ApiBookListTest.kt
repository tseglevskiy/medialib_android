package ru.roscha_akademii.medialib.book.model.remote

import android.support.test.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.roscha_akademii.medialib.AssertsFileHelper
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.MockMediaLibApplication
import ru.roscha_akademii.medialib.net.NetModule

class ApiBookListTest {
    lateinit var testFile: String
    lateinit var server: MockWebServer

    lateinit var bookApi: BookApi // SUT

    lateinit var injectedBaseUrl: String

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()

        testFile = AssertsFileHelper.readFileFromAsserts(instrumentation, "book.json")

        val app = instrumentation.targetContext.applicationContext as MockMediaLibApplication

        server = MockWebServer()
        server.enqueue(MockResponse().setBody(testFile))
        server.start()

        val baseUrl = server.url("/tseglevskiy/medialib_android/")

        val component = DaggerApplicationComponent
                .builder()
                .androidModule(AndroidModule(app, app.refWatcher))
                .netModule(object : NetModule() {
                    override fun baseUrl(): String {
                        return baseUrl.toString()
                    }
                })
                .build()

        app.setTestComponent(component)

        bookApi = component.bookApi()

        injectedBaseUrl = component.serverBaseUrl()

    }

    @Test
    fun assetsFileExists() {
        assertTrue("test file should be exists", testFile.length > 10)
    }

    @Test
    fun webServerMocked() {
        assertFalse("web server didn't mocked", injectedBaseUrl == NetModule.URL)
    }

    @Test
//    @Throws(IOException::class, InterruptedException::class)
    fun getListOfVideos() {
        val call = bookApi.bookList()
        val response = call.execute()

        assertTrue(response.isSuccessful)
        assertEquals(200, response.code().toLong())

        val request1 = server.takeRequest()
        assertEquals("/tseglevskiy/medialib_android/app/src/androidTest/assets/book.json", request1.path)

        val books = response.body().list
        assertNotNull(books)
        assertEquals(8, books!!.size.toLong())

        val v1 = books[0]
        assertEquals(267, v1.id)
        assertEquals("И. Скоморох \"Женитьба дурака\" (электронная версия в формате ePub)", v1.title)
        assertEquals("", v1.picture)
        assertEquals("", v1.description)
//        assertTrue(v1.description!!.length > 10)
    }


}