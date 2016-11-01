package ru.roscha_akademii.medialib.common

import android.content.Context
import android.content.Intent

import javax.inject.Inject
import javax.inject.Singleton

import ru.roscha_akademii.medialib.update.UpdateService
import ru.roscha_akademii.medialib.video.showlist.list.view.ListOfVideoActivity

@Singleton
open class ServiceNavigator
@Inject
internal constructor(private val context: Context) {

    open fun startUpdate() {
        context.startService(
                UpdateService.getStartIntent(context))
    }

    open fun startVideoList() {
        val intent = ListOfVideoActivity.getStartIntent(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
