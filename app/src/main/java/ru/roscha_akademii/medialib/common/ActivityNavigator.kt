package ru.roscha_akademii.medialib.common

import android.app.Activity

import ru.roscha_akademii.medialib.viewvideo.view.ShowVideoActivity

open class ActivityNavigator internal constructor(private val activity: Activity) {

    open fun openVideo(id: Long) {
        activity.startActivity(ShowVideoActivity.getStartIntent(activity, id))
    }
}
