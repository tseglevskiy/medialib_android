package ru.roscha_akademii.medialib.common

import android.app.Application

import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

import io.fabric.sdk.android.Fabric

open class MediaLibApplication : Application() {
    private var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        refWatcher = LeakCanary.install(this)
        Fabric.with(this, Crashlytics())
    }

    protected var component: ApplicationComponent? = null

    private fun createComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().androidModule(AndroidModule(this, refWatcher)).build()
    }

    fun component(): ApplicationComponent? {
        if (component == null) {
            synchronized(this) {
                if (component == null) {
                    component = createComponent()
                }
            }
        }

        return component
    }

    fun refWatcher(): RefWatcher? {
        return refWatcher
    }

}
