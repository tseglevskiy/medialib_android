package ru.roscha_akademii.medialib.video.playvideo.viewvideo.view

import com.arellomobile.mvp.MvpView

interface ShowVideoView : MvpView {
    fun showVideo(url: String)

    fun showStatus(url: String)

    fun showDescription(desc: String?)

    fun showTitle(title: String?)
}
