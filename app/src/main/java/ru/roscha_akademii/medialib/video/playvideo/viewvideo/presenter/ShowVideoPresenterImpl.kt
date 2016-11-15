package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

class ShowVideoPresenterImpl(val videoDb: VideoDb,
                             val storage: Storage)
: MvpBasePresenter<ShowVideoView>(), ShowVideoPresenter {

    lateinit var video: Video

    override fun start(videoId: Long) {
        video = videoDb.getVideo(videoId)

        storage.checkDownloadStatus(video.videoUrl)
        storage.checkLocalUri(video.videoUrl)

        val url = storage.getLocalUriIfAny(video.videoUrl)

        view?.let {
            it.showVideo(url)
            it.showStatus(video.videoUrl)

            it.showDescription(video.description)
            it.showTitle(video.title)
        }
    }
}
