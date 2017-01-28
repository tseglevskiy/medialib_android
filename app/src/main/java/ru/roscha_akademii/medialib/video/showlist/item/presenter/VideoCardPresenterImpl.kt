package ru.roscha_akademii.medialib.video.showlist.item.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.showlist.item.view.VideoCardView

@InjectViewState
class VideoCardPresenterImpl(val videoDb: VideoDb, val storage: Storage)
    : MvpPresenter<VideoCardView>(), VideoCardPresenter {
    override var videoId: Long? = null
        set(value) {
            field = value
            updateView()
        }

    fun updateView() {
        videoId?.let {
            val video = videoDb.getVideo(it)
//            view?.showDescription(video.description ?: "-")

            with(video) {
                viewState.showTitle(title ?: "-")
                viewState.showDate(issueDate)
                viewState.showDuration(duration)
                viewState.showImage(pictureUrl?.let { storage.getLocalUriIfAny(it) })
                viewState.showStatus(videoUrl)
            }
        }
    }

    companion object {
        val MSG_UPDATE = 1
    }
}
