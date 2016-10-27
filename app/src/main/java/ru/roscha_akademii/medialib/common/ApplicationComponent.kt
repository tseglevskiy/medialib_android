package ru.roscha_akademii.medialib.common

import android.content.Context

import com.pushtorefresh.storio.sqlite.StorIOSQLite

import javax.inject.Named
import javax.inject.Singleton

import dagger.Component
import ru.roscha_akademii.medialib.net.NetModule
import ru.roscha_akademii.medialib.net.VideoApi
import ru.roscha_akademii.medialib.update.UpdateModule
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.update.UpdateService
import ru.roscha_akademii.medialib.video.VideoDb
import ru.roscha_akademii.medialib.video.VideoDbSqliteHelper
import ru.roscha_akademii.medialib.video.VideoDbModule

const val BASE_URL = "baseurl"
const val VIDEO_DB = "videodb"
const val VIDEO_DB_FILENAME = "video db filename"

@Singleton
@Component(modules = arrayOf(AndroidModule::class, NetModule::class, VideoDbModule::class, UpdateModule::class))
interface ApplicationComponent {
    /*
    ActivityScope components
     */

    fun activityComponent(am: ActivityModule): ActivityComponent

    /*
    Injectors
     */

    fun inject(updateService: UpdateService)

    /*
    Using submodules requires providing explicit methods in the parent component.
     */

    // AndroidModule

    fun context(): Context

    // NetModule

    @Named(BASE_URL)
    fun serverBaseUrl(): String

    fun videoApi(): VideoApi

    // VideoDbModule

    @Named(VIDEO_DB_FILENAME)
    fun videoDbFileName(): String

    fun videoDbSqliteHelper(): VideoDbSqliteHelper

    @Named(VIDEO_DB)
    fun videoDbStorIo(): StorIOSQLite

    fun videoDb(): VideoDb

    // UpdateModule

    fun updateScheduler(): UpdateScheduler

}
