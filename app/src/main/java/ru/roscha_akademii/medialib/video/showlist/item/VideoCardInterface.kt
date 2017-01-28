package ru.roscha_akademii.medialib.video.showlist.item

import com.arellomobile.mvp.MvpDelegate

interface VideoCardInterface {
    var videoId: Long?

    var parentDeleagate: MvpDelegate<*>?
}
