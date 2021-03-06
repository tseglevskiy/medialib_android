package ru.roscha_akademii.medialib.common

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources

import com.squareup.leakcanary.RefWatcher
import com.squareup.picasso.Picasso

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus

@Module
open class AndroidModule(private val application: MediaLibApplication,
                         private val refWatcher: RefWatcher) {

    @Provides
    @Singleton
    internal fun providesContext(): Context = application

    @Provides
    @Singleton
    internal fun providesRefWatcher() = refWatcher

    @Provides
    @Singleton
    internal fun providesResources() = application.resources

    @Provides
    @Singleton
    internal open fun providesDownloadManager()
            = application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    @Provides
    @Singleton
    internal fun providesContentResolver() = application.contentResolver

    @Provides
    @Singleton
    internal fun providesSharedPreferences(context: Context)
            = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    internal fun providesPicasso(context: Context) = Picasso.with(context)

    @Provides
    @Singleton
    internal fun providesEventBus() = EventBus.getDefault()
}
