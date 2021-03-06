package ru.roscha_akademii.medialib.common

import android.content.Context
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import dagger.Component
import ru.roscha_akademii.medialib.book.BookModule
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.local.BookDbSqliteHelper
import ru.roscha_akademii.medialib.book.model.remote.BookApi
import ru.roscha_akademii.medialib.book.showlist.item.view.BookCard
import ru.roscha_akademii.medialib.net.NetModule
import ru.roscha_akademii.medialib.storage.StorageModule
import ru.roscha_akademii.medialib.video.model.remote.VideoApi
import ru.roscha_akademii.medialib.update.UpdateModule
import ru.roscha_akademii.medialib.update.UpdateScheduler
import ru.roscha_akademii.medialib.update.UpdateService
import ru.roscha_akademii.medialib.video.event.DownloadNotificationClickReceiver
import ru.roscha_akademii.medialib.video.model.local.VideoDb
import ru.roscha_akademii.medialib.video.model.VideoDbModule
import ru.roscha_akademii.medialib.video.model.local.VideoDbSqliteHelper
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.model.StorageDbSqliteHelper
import ru.roscha_akademii.medialib.storage.widget.view.DownloadControl
import ru.roscha_akademii.medialib.video.showlist.item.view.VideoCard
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidModule::class, NetModule::class, VideoDbModule::class, UpdateModule::class, StorageModule::class, BookModule::class))
interface ApplicationComponent {
    /*
    ActivityScope components
     */

    fun activityComponent(am: ActivityModule): ActivityComponent

    /*
    Injectors
     */

    fun inject(updateService: UpdateService)
    fun inject(videoCard: VideoCard)
    fun inject(downloadNotificationClickReceiver: DownloadNotificationClickReceiver)
    fun inject(downloadControl: DownloadControl)
    fun inject(bookCard: BookCard)

    /*
    Using submodules requires providing explicit methods in the parent component.
     */

    // AndroidModule

    fun context(): Context

    // NetModule

    @Named("baseurl")
    fun serverBaseUrl(): String

    fun videoApi(): VideoApi

    fun bookApi(): BookApi

    // VideoDbModule

    @Named("video db filename")
    fun videoDbFileName(): String

    fun videoDbSqliteHelper(): VideoDbSqliteHelper

    @Named("video db")
    fun videoDbStorIo(): StorIOSQLite

    fun videoDb(): VideoDb

    // BookModule
    @Named("book db filename")
    fun bookDbFileName(): String

    fun bookDbSqliteHelper(): BookDbSqliteHelper

    @Named("book db")
    fun bookDbStorIo(): StorIOSQLite

    fun bookDb(): BookDb

    // StorageModule

    fun storageDbHelper(): StorageDbSqliteHelper

    @Named("storage db")
    fun storageDbStorIo(): StorIOSQLite

    fun videoStorage(): Storage

    // UpdateModule

    fun updateScheduler(): UpdateScheduler

}
