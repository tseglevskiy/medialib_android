package ru.roscha_akademii.medialib.video.showlist.list.presenter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoView
import ru.roscha_akademii.medialib.video.showlist.list.view.`ListOfVideoView$$State`
import java.util.*

class MainPresenterImplTest {
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


    lateinit var presenter: MainPresenterImpl // SUT

    lateinit var updateScheduler: UpdateScheduler
    lateinit var view: ListOfVideoView
    lateinit var viewState: `ListOfVideoView$$State`
    lateinit var videoDb: VideoDb
    lateinit var navigator: ActivityNavigator

    @Before
    fun setUp() {
        updateScheduler = mock<UpdateScheduler>()
        videoDb = mock<VideoDb>()
        navigator = mock<ActivityNavigator>()
        view = mock<ListOfVideoView>()
        viewState = mock<`ListOfVideoView$$State`>()

        presenter = MainPresenterImpl(updateScheduler, videoDb, navigator)
        presenter.attachView(view)
        presenter.setViewState(viewState)
    }

    @Test
    fun start() {
        verify(updateScheduler, times(1)).startBySchedule()
    }

    @Test
    @Ignore
    fun displayVideoList() {
        val srcList = ArrayList<Video>()
        srcList.add(video1)
        srcList.add(video2)

//        whenever(videoDb.getAllVideo()).thenReturn(srcList)

//        presenter.attachView(view)
//        presenter.start()

//        verify(view).showVideoList(any())
//        verify(view, times(1)).showVideoList(anyObject())

//        val listCaptor = ArgumentCaptor.forClass(List<Video>::class.java)
//        verify<ListOfVideoView>(view, times(1)).showVideoList(listCaptor.capture())

//        val dstList = listCaptor.value as ArrayList<Video>
//        assertEquals(2, dstList.size.toLong())
//        assertEquals(video1.id, dstList[0].id)
//        assertEquals(video2.id, dstList[1].id)

    }


}