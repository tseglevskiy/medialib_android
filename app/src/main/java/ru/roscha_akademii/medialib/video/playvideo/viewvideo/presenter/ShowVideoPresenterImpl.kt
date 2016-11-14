package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import android.util.Log
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.storage.StorageStatus

import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

class ShowVideoPresenterImpl(val videoDb: VideoDb,
                             val storage: Storage)
: MvpBasePresenter<ShowVideoView>(), ShowVideoPresenter {

    override fun start(videoId: Long) {
        val video = videoDb.getVideo(videoId)

        storage.checkDownloadStatus(video.videoUrl)
        storage.checkLocalUri(video.videoUrl)

        val url = storage.getLocalUriIfAny(video.videoUrl)
        Log.d("happy", url)

        view?.showVideo(url)
    }
}
