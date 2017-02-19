package ru.roscha_akademii.medialib.video.showlist.list.presenter

import com.nhaarman.mockito_kotlin.*
import org.greenrobot.eventbus.EventBus
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoView
import ru.roscha_akademii.medialib.video.showlist.list.view.`ListOfVideoView$$State`
import java.util.*

class VideoListPresenterImplTest {
    /*
     * test data
     */
    val video1 = Video(
            id = 1111,
            title = "title one",
            pictureUrl = "picture url one",
            description = "description one",
            videoUrl = "video url one",
            issueDate = LocalDate.parse("2000-01-01"),
            duration = "0:01")

    val video2 = Video(
            id = 2222,
            title = "title two",
            pictureUrl = "picture url two",
            description = "description two",
            videoUrl = "video url two",
            issueDate = LocalDate.parse("2001-02-02"),
            duration = "0:02")


    lateinit var presenter: VideoListPresenterImpl // SUT

    @Mock
    lateinit var view: ListOfVideoView

    @Mock
    lateinit var viewState: `ListOfVideoView$$State`

    @Mock
    lateinit var videoDb: VideoDb

    @Mock
    lateinit var navigator: ActivityNavigator

    @Mock
    lateinit var bus: EventBus

    @Captor
    lateinit var listCaptor: ArgumentCaptor<List<Video>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = VideoListPresenterImpl(bus, videoDb, navigator) // SUT
    }

    @Test
    fun registerBus() {
        presenter.setViewState(viewState)
        presenter.attachView(view)

        verify(bus).register(presenter)
        verifyNoMoreInteractions(bus)
    }

    @Test
    fun unregisterBus() {
        presenter.setViewState(viewState)
        presenter.attachView(view)
        presenter.onDestroy()

        verify(bus).unregister(presenter)
    }

    @Test
    fun requestVideoList() {
        presenter.setViewState(viewState)
        presenter.attachView(view)

        verify(videoDb).allVideo
    }

    @Test
    fun displayVideoList() {
        val srcList = ArrayList<Video>()
        srcList.add(video1)
        srcList.add(video2)

        whenever(videoDb.allVideo).thenReturn(srcList)

        presenter.setViewState(viewState)
        presenter.attachView(view)

        verify(viewState, times(1)).showVideoList(capture(listCaptor))

        val dstList = listCaptor.allValues[0]

        assertEquals(srcList, dstList)
        assertEquals(2, dstList.size.toLong())

//        assertEquals(book1.id, dstList[0].id)
//        assertEquals(video2.id, dstList[1].id)

    }


}