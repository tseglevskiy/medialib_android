package ru.roscha_akademii.medialib.video.showlist.item.presenter

import android.os.Handler
import android.os.Message
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.storage.StorageStatus
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.showlist.item.view.VideoCardView

class VideoCardPresenterImpl(val videoDb: VideoDb, val storage: Storage)
: MvpBasePresenter<VideoCardView>(), VideoCardPresenter {
    override var videoId: Long? = null
        set(value) {
            field = value
            updateView()
        }

    override fun stop() {
        updateHandler.stop()
    }

    fun updateView() {
        if (videoId != null) {
            val video = videoDb.getVideo(videoId!!)
            view?.showDescription(video.description ?: "-")
            view?.showTitle(video.title ?: "-")
            updateStatus()
        } else {
            view?.showDescription("-")
            view?.showTitle("-")
        }

    }

    override fun updateStatus() {
        if (videoId != null) {
            val video = videoDb.getVideo(videoId!!)

            val status = storage.getStatus(video.videoUrl)
            val percent = storage.getPercent(video.videoUrl)
            view?.showStatus(status, percent)

            if (status == StorageStatus.PROGRESS && view != null) {
                updateHandler.start()
            }

            view?.showStatus(status, percent)
        } else {
            view?.showStatus(StorageStatus.REMOTE, 0)
        }
    }

    override fun saveLocal() {
        if (videoId == null) return

        val video = videoDb.getVideo(videoId!!)
        storage.saveLocal(video.videoUrl, video.title ?: "", true)

        updateStatus()
        updateHandler.start()
    }

    override fun removeLocal() {
        if (videoId == null) return

        val video = videoDb.getVideo(videoId!!)
        storage.removeLocal(video.videoUrl)

        updateStatus()
    }

    val updateHandler: UpdateHandler by lazy {
        UpdateHandler()
    }

    inner class UpdateHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_UPDATE -> {
                    updateStatus()
                }

                else -> super.handleMessage(msg)
            }
        }

        fun start() {
            removeMessages(MSG_UPDATE)
            sendEmptyMessageDelayed(MSG_UPDATE, 3000)
        }

        fun stop() {
            removeMessages(MSG_UPDATE)
        }


    }

    companion object {
        val MSG_UPDATE = 1
    }
}
