package ru.roscha_akademii.medialib.video.showlist.list.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.common.ActivityNavigator
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoView

@InjectViewState
class MainPresenterImpl(private val videoDb: VideoDb,
                        private val navigator: ActivityNavigator) : MvpPresenter<ListOfVideoView>(), MainPresenter {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getAndDisplayVideoList()
    }

    override fun wannaOpenVideo(id: Long) {
        navigator.openVideo(id)
    }

    private fun getAndDisplayVideoList() {
        val list = videoDb.allVideo
        viewState.showVideoList(list)
    }
}
