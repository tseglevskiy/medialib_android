package ru.roscha_akademii.medialib.storage.widget.view

import com.arellomobile.mvp.MvpView
import ru.roscha_akademii.medialib.storage.model.StorageStatus

interface DownloadControlView : MvpView {
    fun showStatus(status: StorageStatus, percent: Int?)
}