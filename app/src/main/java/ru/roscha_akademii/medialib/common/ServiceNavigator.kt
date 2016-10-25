package ru.roscha_akademii.medialib.common

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import ru.roscha_akademii.medialib.update.UpdateService

@Singleton
class ServiceNavigator
@Inject
internal constructor(private val context: Context) {

    fun startUpdate() {
        context.startService(
                UpdateService.getStartIntent(context))
    }
}
