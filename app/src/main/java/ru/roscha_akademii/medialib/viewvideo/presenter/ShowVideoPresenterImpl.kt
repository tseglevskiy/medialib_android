package ru.roscha_akademii.medialib.viewvideo.presenter

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter

import ru.roscha_akademii.medialib.net.model.Video
import ru.roscha_akademii.medialib.video.VideoDb
import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoView

class ShowVideoPresenterImpl(private val videoDb: VideoDb) : MvpBasePresenter<ShowVideoView>(), ShowVideoPresenter {

    override fun start(videoId: Long) {
        val video = videoDb.getVideo(videoId)

        if (view != null) {
            view!!.showVideo(video)
        }
    }
}
