package ru.roscha_akademii.medialib.storage.widget.view

import com.hannesdorfmann.mosby.mvp.MvpView
import ru.roscha_akademii.medialib.storage.StorageStatus

interface DownloadControlView : MvpView {
    fun showStatus(status: StorageStatus, percent: Int?)
}