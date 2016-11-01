package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import android.util.Log
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.video.model.local.StorageStatus

import ru.roscha_akademii.medialib.video.model.remote.Video
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.local.VideoStorage
import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

class ShowVideoPresenterImpl(val videoDb: VideoDb,
                             val videoStorage: VideoStorage)
: MvpBasePresenter<ShowVideoView>(), ShowVideoPresenter {

    override fun start(videoId: Long) {
        videoStorage.checkDownloadStatus(videoId)
        videoStorage.checkLocalUri(videoId)

        val url = videoStorage.getRecord(videoId)?.localUri ?: videoDb.getVideo(videoId).videoUrl
        Log.d("happy", url)

        view?.showVideo(url)
    }
}
