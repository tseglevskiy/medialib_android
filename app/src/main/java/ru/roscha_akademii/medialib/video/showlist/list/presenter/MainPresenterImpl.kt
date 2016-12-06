package ru.roscha_akademii.medialib.video.showlist.list.presenter

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter

import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoView
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.video.model.local.VideoDb

class MainPresenterImpl(internal var updateScheduler: UpdateScheduler,
                        private val videoDb: VideoDb,
                        private val navigator: ActivityNavigator) : MvpBasePresenter<ListOfVideoView>(), MainPresenter {

    override fun wannaUpdateVideoList() {
        updateScheduler.startNow()
//        view?.showHelloToast()
    }

    override fun start() {
        updateScheduler.startBySchedule()
        getAndDisplayVideoList()
    }

    override fun wannaOpenVideo(id: Long) {
        navigator.openVideo(id)
    }

    private fun getAndDisplayVideoList() {
        val list = videoDb.allVideo
        view?.showVideoList(list)
    }
}
