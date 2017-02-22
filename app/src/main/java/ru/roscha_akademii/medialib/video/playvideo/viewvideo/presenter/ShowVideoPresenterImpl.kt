package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.entity.Video
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

@InjectViewState
class ShowVideoPresenterImpl(val videoDb: VideoDb,
                             val storage: Storage)
    : MvpPresenter<ShowVideoView>(), ShowVideoPresenter {

    lateinit var video: Video

    override fun start(videoId: Long) {
        video = videoDb.getVideo(videoId)

        with(video) {
            storage.checkDownloadStatus(videoUrl)
            storage.checkLocalUri(videoUrl)

            viewState.showStatus(videoUrl, title)
            viewState.showDescription(description)

            val url = storage.getLocalUriIfAny(videoUrl)
            viewState.showVideo(url)
        }
    }
}
