package ru.roscha_akademii.medialib.video.showlist.list.view

import com.arellomobile.mvp.MvpView
import ru.roscha_akademii.medialib.video.model.remote.entity.Video

interface ListOfVideoView : MvpView {
    fun showHelloToast()

    fun showVideoList(list: List<Video>)
}
