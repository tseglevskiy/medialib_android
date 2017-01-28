package ru.roscha_akademii.medialib.storage.widget

import com.arellomobile.mvp.MvpDelegate

interface DownloadControlInterface {
    var parentDelegate: MvpDelegate<*>?

    var url: String?

    var title: String?
}
