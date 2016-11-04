package ru.roscha_akademii.medialib.common

import android.app.Application
import android.os.StrictMode
import com.crashlytics.android.BuildConfig

import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

import io.fabric.sdk.android.Fabric

open class MediaLibApplication : Application() {
    var _component: ApplicationComponent? = null
    val component: ApplicationComponent
        get() {
            return _component!!
        }

    lateinit var refWatcher: RefWatcher

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .penaltyDialog()
                            .penaltyDeathOnNetwork()
                            .build()
            )
            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .penaltyDeath()
                            .build()
            )
        }

        super.onCreate()

        refWatcher = LeakCanary.install(this)

        if (_component == null) {
            _component = DaggerApplicationComponent
                    .builder()
                    .androidModule(AndroidModule(this, refWatcher))
                    .build()
        }

        Fabric.with(this, Crashlytics())
    }
}
