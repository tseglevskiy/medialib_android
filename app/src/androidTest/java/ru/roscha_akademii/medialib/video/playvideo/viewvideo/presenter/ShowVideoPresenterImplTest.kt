package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.anyLong
import org.mockito.Mockito.*
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.stub.StorageStub
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

class ShowVideoPresenterImplTest {

    lateinit var videoDb: VideoDb
    lateinit var presenter: ShowVideoPresenterImpl
    lateinit var view: ShowVideoView
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
        videoDb = mock(VideoDb::class.java)
        storage = StorageStub()
        view = mock(ShowVideoView::class.java)

        presenter = ShowVideoPresenterImpl(videoDb, storage) // SUT
    }


    @Test
    @Throws(Exception::class)
    fun start() {
        `when`(videoDb.getVideo(anyLong())).thenReturn(video1)

        presenter.attachView(view)
        presenter.start(video1.id)

        val longCaptor = ArgumentCaptor.forClass(Long::class.java)
        val videoCaptor = ArgumentCaptor.forClass(String::class.java)

        verify<VideoDb>(videoDb, times(1)).getVideo(longCaptor.capture())
        assertEquals(video1.id.toLong(), longCaptor.allValues[0])

//        verify<ShowVideoView>(view, times(1)).showVideo(videoCaptor.capture())
//        assertEquals(video1.videoUrl, videoCaptor.allValues[0])

    }


}