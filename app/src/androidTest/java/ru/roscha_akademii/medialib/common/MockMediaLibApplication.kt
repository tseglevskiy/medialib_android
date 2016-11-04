package ru.roscha_akademii.medialib.common

import android.util.Log

class MockMediaLibApplication : MediaLibApplication() {

    fun setTestComponent(testComponent: ApplicationComponent)
    {
        _component = testComponent
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("happy", "MockMediaLibApplication is running")
    }
}