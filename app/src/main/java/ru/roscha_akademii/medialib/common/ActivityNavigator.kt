package ru.roscha_akademii.medialib.common

import android.app.Activity
import ru.roscha_akademii.medialib.book.onebook.view.OneBookActivity

import ru.roscha_akademii.medialib.video.playvideo.viewvideo.view.ShowVideoActivity

open class ActivityNavigator internal constructor(private val activity: Activity) {

    open fun openVideo(id: Long) {
        activity.startActivity(ShowVideoActivity.getStartIntent(activity, id))
    }

    open fun openBook(id: Long) {
        activity.startActivity(OneBookActivity.getStartIntent(activity, id))
    }
}
