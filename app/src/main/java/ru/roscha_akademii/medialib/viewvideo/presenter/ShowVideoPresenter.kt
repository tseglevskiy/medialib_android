package ru.roscha_akademii.medialib.viewvideo.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter

import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoView

interface ShowVideoPresenter : MvpPresenter<ShowVideoView> {
    fun start(videoId: Long)
}
