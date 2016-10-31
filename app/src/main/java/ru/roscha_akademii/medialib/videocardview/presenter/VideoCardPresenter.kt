package ru.roscha_akademii.medialib.videocardview.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter
import ru.roscha_akademii.medialib.videocardview.view.VideoCardView

interface VideoCardPresenter : MvpPresenter<VideoCardView> {
    var videoId: Long?

    fun stop()

    fun updateStatus()

    fun saveLocal()

    fun removeLocal()

}