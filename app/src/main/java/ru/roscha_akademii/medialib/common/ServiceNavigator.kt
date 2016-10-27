package ru.roscha_akademii.medialib.common

import android.content.Context

import javax.inject.Inject
import javax.inject.Singleton

import ru.roscha_akademii.medialib.update.UpdateService

@Singleton
open class ServiceNavigator
@Inject
internal constructor(private val context: Context) {

    open fun startUpdate() {
        context.startService(
                UpdateService.getStartIntent(context))
    }
}
