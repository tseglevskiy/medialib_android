package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import com.nhaarman.mockito_kotlin.mock
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.anyLong
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.stub.StorageStub
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.`ShowVideoView$$State`

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class ShowVideoPresenterImplTest {

    lateinit var videoDb: VideoDb
    lateinit var presenter: ShowVideoPresenterImpl
    lateinit var view: ShowVideoView
    lateinit var viewState: `ShowVideoView$$State`
    lateinit var storage: Storage

    private val video1 = Video(
            id = 1111,
            title = "title one",
            pictureUrl = "picture url one",
            description = "description one",
            videoUrl = "video url one",
            issueDate = LocalDate.parse("2000-01-01"),
            duration = "0:01")

    @Before
    @Throws(Exception::class)
    fun setUp() {
//        val app = RuntimeEnvironment.application as RobolectricMdiaLibApplication

        videoDb = mock<VideoDb>()
        storage = StorageStub()
        view = mock<ShowVideoView>()
        viewState = mock<`ShowVideoView$$State`>()

        presenter = ShowVideoPresenterImpl(videoDb, storage) // SUT
    }

    @Test
    fun attachView() {
        presenter.attachView(view)
        presenter.setViewState(viewState)

        verifyNoMoreInteractions(view)
        verifyNoMoreInteractions(viewState)
    }

    @Test
    @Throws(Exception::class)
    fun start() {
        `when`(videoDb.getVideo(anyLong())).thenReturn(video1)

        presenter.attachView(view)
        presenter.setViewState(viewState)
        presenter.start(video1.id)

        val longCaptor = ArgumentCaptor.forClass(Long::class.java)
        val videoCaptor = ArgumentCaptor.forClass(String::class.java)

        verify<VideoDb>(videoDb, times(1)).getVideo(longCaptor.capture())
        assertEquals(video1.id.toLong(), longCaptor.allValues[0])

//        verify<ShowVideoView>(view, times(1)).showVideo(videoCaptor.capture())
//        assertEquals(book1.videoUrl, videoCaptor.allValues[0])

    }


}