package ru.roscha_akademii.medialib.storage.widget.presenter

interface DownloadControlPresenter {
    var url: String?

    var title: String?

    fun saveLocal()

    fun removeLocal()

    fun updateStatus()

    fun stop()
}