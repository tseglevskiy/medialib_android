package ru.roscha_akademii.medialib.update

import android.content.SharedPreferences

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import ru.roscha_akademii.medialib.common.ServiceNavigator
import ru.roscha_akademii.medialib.common.TimeProvider

@Module
open class UpdateModule {
    @Provides
    @Singleton
    open fun provides(prefs: SharedPreferences,
                 timeProvider: TimeProvider,
                 navigator: ServiceNavigator): UpdateScheduler {
        return UpdateScheduler(prefs, timeProvider, navigator)
    }
}
