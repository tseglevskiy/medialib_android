package ru.roscha_akademii.medialib.main.presenter

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter

import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.main.view.MainView
import ru.roscha_akademii.medialib.net.model.Video
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.video.VideoDb

class MainPresenterImpl(internal var updateScheduler: UpdateScheduler,
                        private val videoDb: VideoDb,
                        private val navigator: ActivityNavigator) : MvpBasePresenter<MainView>(), MainPresenter {

    override fun wannaUpdateVideoList() {
        updateScheduler.startNow()
        if (view != null) {
            view!!.showHelloToast()
        }
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
        if (view != null) {
            view!!.showVideoList(list)
        }
    }
}
