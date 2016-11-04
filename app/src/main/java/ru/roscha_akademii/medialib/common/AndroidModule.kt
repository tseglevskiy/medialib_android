package ru.roscha_akademii.medialib.common

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources

import com.squareup.leakcanary.RefWatcher

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val application: MediaLibApplication, private val refWatcher: RefWatcher) {

    @Provides
    @Singleton
    internal fun providesContext(): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun providesRefWatcher(): RefWatcher {
        return refWatcher
    }

    @Provides
    @Singleton
    internal fun providesResources(): Resources {
        return application.resources
    }

    @Provides
    @Singleton
    internal fun providesDownloadManager(): DownloadManager {
        return application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    @Provides
    @Singleton
    internal fun providesContentResolver(): ContentResolver {
        return application.contentResolver
    }

            @Provides
    @Singleton
    internal fun providesSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE)
    }

}
