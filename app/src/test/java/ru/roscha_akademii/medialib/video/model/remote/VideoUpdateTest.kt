package ru.roscha_akademii.medialib.video.model.remote

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.whenever
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.update.UpdateCallback
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import ru.roscha_akademii.medialib.video.model.remote.entity.VideoAnswer
import java.util.*


//@RunWith(RobolectricTestRunner::class)
//@Config(constants = BuildConfig::class,
//        sdk = intArrayOf(21),
//        manifest = "AndroidManifest.xml",
//        application = RobolectricMdiaLibApplication::class,
//        packageName = "ru.roscha_akademii.medialib")

class VideoUpdateTest {
    val video1 = Video(
            id = 1111,
            title = "title one",
            pictureUrl = "picture url one",
            description = "description one",
            videoUrl = "video url one",
            duration = "0:05",
            issueDate = LocalDate.parse("2000-01-02"))

    @Mock
    lateinit var videoApi: VideoApi


    @Mock
    lateinit var storage: Storage

    @Mock
    lateinit var call: Call<VideoAnswer>

    @Mock
    lateinit var callback: UpdateCallback

    @Captor
    lateinit var requestCallCaptor: ArgumentCaptor<Callback<VideoAnswer>>

    @Captor
    lateinit var videoListCaptor: ArgumentCaptor<ArrayList<Video>>

    @Mock
    lateinit var videoDb: VideoDb

    lateinit var updater: VideoUpdate // SUT

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(videoApi.videoList()).thenReturn(call)

        updater = VideoUpdate(videoApi, videoDb, storage) // SUT
    }

    @Test
    fun runVideoList() {
        updater.update(callback)

        verify(videoApi).videoList()
    }

    @Test
    fun failedResult() {
        updater.update(callback)

        verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        requestCallback.onFailure(call, RuntimeException("hello"))

        verify(callback).onFail()
    }

    @Test
    fun successResultWithEmptyList() {
        updater.update(callback)

        verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        val answer = VideoAnswer()
        val response = Response.success(answer)
        requestCallback.onResponse(call, response)

        verify(videoDb, times(0)).saveVideos(any())
        verify(callback).onSuccess()
    }

    @Test
    fun successResultWithData() {
        updater.update(callback)

        verify(call, times(1)).enqueue(capture(requestCallCaptor))
        val requestCallback = requestCallCaptor.allValues[0]

        val list = ArrayList<Video>()
        list.add(video1)
        val answer = VideoAnswer()
        answer.list = list
        val response = Response.success(answer)
        requestCallback.onResponse(call, response)

        verify(videoDb, times(1)).saveVideos(capture(videoListCaptor))
        assertEquals(list, videoListCaptor.allValues[0])

        verify(callback).onSuccess()
    }
}
