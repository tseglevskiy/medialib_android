package ru.roscha_akademii.medialib.viewvideo.view

import com.hannesdorfmann.mosby.mvp.MvpView

import ru.roscha_akademii.medialib.net.model.Video

interface ShowVideoView : MvpView {
    fun showVideo(video: Video)
}
