package ru.roscha_akademii.medialib.video.showlist.item.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter
import ru.roscha_akademii.medialib.video.showlist.item.view.VideoCardView

interface VideoCardPresenter : MvpPresenter<VideoCardView> {
    var videoId: Long?

    fun stop()

    fun updateStatus()

    fun saveLocal()

    fun removeLocal()

}