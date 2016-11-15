package ru.roscha_akademii.medialib.video.playvideo.viewvideo.view

import com.hannesdorfmann.mosby.mvp.MvpView
import ru.roscha_akademii.medialib.storage.StorageStatus

import ru.roscha_akademii.medialib.video.model.remote.Video

interface ShowVideoView : MvpView {
    fun showVideo(url: String)

    fun showDescription(desc: String?)

    fun showStatus(status: StorageStatus, percent: Int?)

}
