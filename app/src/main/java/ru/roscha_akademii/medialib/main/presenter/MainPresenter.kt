package ru.roscha_akademii.medialib.main.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter

import ru.roscha_akademii.medialib.main.view.MainView

interface MainPresenter : MvpPresenter<MainView> {
    fun wannaUpdateVideoList()

    fun start()

    fun wannaOpenVideo(id: Long)
}
