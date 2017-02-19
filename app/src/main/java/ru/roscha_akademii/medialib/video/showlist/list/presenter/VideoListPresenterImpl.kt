package ru.roscha_akademii.medialib.video.showlist.list.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.update.event.DataDownloaded
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoView

@InjectViewState
class VideoListPresenterImpl(private val bus: EventBus,
                             private val videoDb: VideoDb,
                             private val navigator: ActivityNavigator) : MvpPresenter<ListOfVideoView>(), VideoListPresenter {


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getAndDisplayVideoList()
        bus.register(this)
    }

    override fun wannaOpenVideo(id: Long) {
        navigator.openVideo(id)
    }

    private fun getAndDisplayVideoList() {
        val list = videoDb.allVideo
        viewState.showVideoList(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)
    }

    @Subscribe
    fun onEvent(event: DataDownloaded) {
        getAndDisplayVideoList()
    }
}
