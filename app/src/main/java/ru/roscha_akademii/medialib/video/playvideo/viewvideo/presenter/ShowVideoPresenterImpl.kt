package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

@InjectViewState
class ShowVideoPresenterImpl(val videoDb: VideoDb,
                             val storage: Storage)
    : MvpPresenter<ShowVideoView>(), ShowVideoPresenter {

    lateinit var video: Video

    override fun start(videoId: Long) {
        video = videoDb.getVideo(videoId)

        storage.checkDownloadStatus(video.videoUrl)
        storage.checkLocalUri(video.videoUrl)

        val url = storage.getLocalUriIfAny(video.videoUrl)


        viewState.showStatus(video.videoUrl, video.title)

        viewState.showDescription(video.description)

        viewState.showVideo(url)
    }
}
