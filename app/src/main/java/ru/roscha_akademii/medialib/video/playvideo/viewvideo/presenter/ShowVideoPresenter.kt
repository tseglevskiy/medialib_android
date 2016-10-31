package ru.roscha_akademii.medialib.video.playvideo.viewvideo.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter

import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoView

interface ShowVideoPresenter : MvpPresenter<ShowVideoView> {
    fun start(videoId: Long)
}
