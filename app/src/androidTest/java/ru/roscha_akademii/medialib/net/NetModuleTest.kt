package ru.roscha_akademii.medialib.net

import android.support.test.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.joda.time.LocalDate
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.roscha_akademii.medialib.AssertsFileHelper
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.MockMediaLibApplication
import ru.roscha_akademii.medialib.video.model.remote.VideoApi
import java.io.IOException

class NetModuleTest {
    lateinit var testVideoList: String
    lateinit var server: MockWebServer

    lateinit var videoApi: VideoApi // SUT

    lateinit var injectedBaseUrl: String

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()

        testVideoList = AssertsFileHelper.readFileFromAsserts(instrumentation, "video.json")

        val app = instrumentation.targetContext.applicationContext as MockMediaLibApplication

        server = MockWebServer()
        server.enqueue(MockResponse().setBody(testVideoList))
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

        videoApi = component.videoApi()
        injectedBaseUrl = component.serverBaseUrl()

    }

    @Test
    fun assetsFileExists() {
        assertTrue("test file should be exists", testVideoList.length > 10)
    }

    @Test
    fun webServerMocked() {
        assertFalse("web server didn't mocked", injectedBaseUrl == NetModule.URL)
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun getListOfVideos() {
        val call = videoApi.videoList()
        val response = call.execute()

        assertTrue(response.isSuccessful)
        assertEquals(200, response.code().toLong())

        val request1 = server.takeRequest()
        assertEquals("/tseglevskiy/medialib_android/app/src/androidTest/assets/video.json", request1.path)

        val videos = response.body().list
        assertNotNull(videos)
        assertEquals(3, videos!!.size.toLong())

        val v1 = videos[0]
        assertEquals(13, v1.id)
        assertEquals("Мазыкская игрушка. Елочка", v1.title)
        assertEquals("http://video.roscha-akademii.ru/img/movie/preview/13.jpg", v1.pictureUrl)
        assertEquals("http://video.roscha-akademii.ru/img/movie/video/video1.mp4", v1.videoUrl)
        assertTrue(v1.description!!.length > 10)
        assertEquals("0:43 мин.", v1.duration)
        assertEquals(LocalDate.parse("2016-02-16"), v1.issueDate)
    }

}