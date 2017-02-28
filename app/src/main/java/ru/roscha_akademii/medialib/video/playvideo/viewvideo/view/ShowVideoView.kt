package ru.roscha_akademii.medialib.video.playvideo.viewvideo.view

import com.arellomobile.mvp.MvpView
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile

interface ShowVideoView : MvpView {
    fun showVideo(url: String)

    fun showStatus(url: String, title: String?)

    fun showDescription(desc: String?)
}
