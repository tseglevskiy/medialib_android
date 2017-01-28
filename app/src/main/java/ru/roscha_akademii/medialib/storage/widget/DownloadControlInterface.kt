package ru.roscha_akademii.medialib.storage.widget

import com.arellomobile.mvp.MvpDelegate

interface DownloadControlInterface {
    fun downloadUrl(parentDelegate: MvpDelegate<*>, url: String, title: String?)
}
