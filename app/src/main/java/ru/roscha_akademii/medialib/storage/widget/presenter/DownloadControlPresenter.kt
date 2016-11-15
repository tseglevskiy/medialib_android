package ru.roscha_akademii.medialib.storage.widget.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter
import ru.roscha_akademii.medialib.storage.widget.view.DownloadControlView

interface DownloadControlPresenter : MvpPresenter<DownloadControlView> {
    var url: String?

    var title: String?

    fun saveLocal()

    fun removeLocal()

    fun updateStatus()

    fun stop()
}