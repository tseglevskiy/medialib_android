package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter

import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

class ShowVideoPresenterImpl(private val videoDb: VideoDb) : MvpBasePresenter<ShowVideoView>(), ShowVideoPresenter {

    override fun start(videoId: Long) {
        val video = videoDb.getVideo(videoId)

        view?.showVideo(video)
    }
}
