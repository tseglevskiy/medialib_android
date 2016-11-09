package ru.roscha_akademii.medialib.common

// MockAppRunner

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

class MockAppRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, MockMediaLibApplication::class.java.name, context)
    }
}

