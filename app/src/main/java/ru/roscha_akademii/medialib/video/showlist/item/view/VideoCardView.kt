package ru.roscha_akademii.medialib.video.showlist.item.view

import com.hannesdorfmann.mosby.mvp.MvpView
import ru.roscha_akademii.medialib.storage.StorageStatus

interface VideoCardView : MvpView {
    fun showTitle(title: String)

    fun showDescription(desc: String)

    fun showStatus(status: StorageStatus, percent: Int?)

}
