package ru.roscha_akademii.medialib.update

import android.content.SharedPreferences

import java.util.concurrent.TimeUnit

import ru.roscha_akademii.medialib.common.ServiceNavigator
import ru.roscha_akademii.medialib.common.TimeProvider

open class UpdateScheduler constructor(private val prefs: SharedPreferences,
                                           private val timeProvider: TimeProvider,
                                           private val navigator: ServiceNavigator) {



    open fun startBySchedule() {
        val last = prefs.getLong(PREFS_UPDATE_SCHEDULE, 0)
        val now = timeProvider.currentTimeMillis()
        if (now - last > UPDATE_TIMEOUT) {
            startNow()
        }
    }

    open fun startNow() {
        navigator.startUpdate()
    }

    open fun updateCompleted() {
        val now = timeProvider.currentTimeMillis()
        prefs.edit().putLong(PREFS_UPDATE_SCHEDULE, now).apply()
    }

    companion object Constants {
        val UPDATE_TIMEOUT = TimeUnit.DAYS.toMillis(1)
        val PREFS_UPDATE_SCHEDULE = "PREFS_UPDATE_SCHEDULE"
    }
}
