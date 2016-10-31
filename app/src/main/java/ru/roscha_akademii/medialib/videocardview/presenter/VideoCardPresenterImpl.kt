package ru.roscha_akademii.medialib.videocardview.presenter

import android.util.Log
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.video.StorageStatus
import ru.roscha_akademii.medialib.video.VideoDb
import ru.roscha_akademii.medialib.video.VideoStorage
import ru.roscha_akademii.medialib.videocardview.view.VideoCardView

class VideoCardPresenterImpl(val videoDb: VideoDb, val videoStorage: VideoStorage)
: MvpBasePresenter<VideoCardView>(), VideoCardPresenter {
    override var videoId: Long? = null
        set(value) {
            field = value
            updateView()
        }

    override fun stop() {

    }

    fun updateView() {
        if (videoId != null) {
            val video = videoDb.getVideo(videoId!!)
            view?.showDescription(video.description)
            view?.showTitle(video.title)
            updateStatus()
        } else {
            view?.showDescription("-")
            view?.showTitle("-")
        }

    }

    override fun updateStatus() {
        if (videoId != null) {
            val status = videoStorage.getStatus(videoId!!)
            val percent = if (status == StorageStatus.PROGRESS) videoStorage.getPercent(videoId!!) else 0
            view?.showStatus(status, percent)
        } else {
            view?.showStatus(StorageStatus.REMOTE, 0)
        }
    }

    override fun saveLocal() {
        Log.d("happy", "saveLocal()")
    }

    override fun removeLocal() {
        Log.d("happy", "removeLocal()")
    }


}
