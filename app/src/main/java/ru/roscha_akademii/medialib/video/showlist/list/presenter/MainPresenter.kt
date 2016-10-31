package ru.roscha_akademii.medialib.video.showlist.list.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter

import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoView

interface MainPresenter : MvpPresenter<ListOfVideoView> {
    fun wannaUpdateVideoList()

    fun start()

    fun wannaOpenVideo(id: Long)
}
