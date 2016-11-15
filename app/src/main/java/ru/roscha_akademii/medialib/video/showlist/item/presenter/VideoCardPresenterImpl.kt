package ru.roscha_akademii.medialib.video.showlist.item.presenter

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.showlist.item.view.VideoCardView

class VideoCardPresenterImpl(val videoDb: VideoDb, val storage: Storage)
: MvpBasePresenter<VideoCardView>(), VideoCardPresenter {
    override var videoId: Long? = null
        set(value) {
            field = value
            updateView()
        }

    fun updateView() {
        videoId?.let {
            val video = videoDb.getVideo(it)
//            view?.showDescription(video.description ?: "-")

            view?.let {
                with(video) {
                    it.showTitle(title ?: "-")
                    it.showDate(issueDate)
                    it.showDuration(duration)
                    it.showImage(pictureUrl?.let { storage.getLocalUriIfAny(it) })
                    it.showStatus(videoUrl)
                }
            }
        }
    }

    companion object {
        val MSG_UPDATE = 1
    }
}
