package ru.roscha_akademii.medialib.video.showlist.list.view

import com.hannesdorfmann.mosby.mvp.MvpView

import ru.roscha_akademii.medialib.video.model.remote.Video

interface ListOfVideoView : MvpView {
    fun showHelloToast()

    fun showVideoList(list: List<Video>)
}
