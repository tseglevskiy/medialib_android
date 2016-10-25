package ru.roscha_akademii.medialib.main.view

import com.hannesdorfmann.mosby.mvp.MvpView

import ru.roscha_akademii.medialib.net.model.Video

interface MainView : MvpView {
    fun showHelloToast()

    fun showVideoList(list: List<Video>)
}
