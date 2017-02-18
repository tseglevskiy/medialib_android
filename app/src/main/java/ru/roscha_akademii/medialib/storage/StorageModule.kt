package ru.roscha_akademii.medialib.storage

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite
import dagger.Module
import dagger.Provides
import ru.roscha_akademii.medialib.storage.model.StorageDbSqliteHelper
import ru.roscha_akademii.medialib.storage.model.StorageRecordMapping
import ru.roscha_akademii.medialib.storage.model.FileStorageRecord
import javax.inject.Named
import javax.inject.Singleton

@Module
open class StorageModule {

    @Provides
    @Singleton
    internal fun providesVideoStorage(@Named("storage db") storio: StorIOSQLite,
                                      context: Context,
                                      contentResolver: ContentResolver,
                                      downloadManager: DownloadManager) : Storage
            = StorageImpl(
            db = storio,
            context = context,
            contentResolver = contentResolver,
            downloadManager = downloadManager)


    @Provides
    @Singleton
    @Named("storage db")
    internal fun providesStorageDbSio(dbHelper: StorageDbSqliteHelper)
            : StorIOSQLite
            = DefaultStorIOSQLite
            .builder()
            .sqliteOpenHelper(dbHelper)
            .addTypeMapping(FileStorageRecord::class.java, StorageRecordMapping())
            .build()

    @Provides
    @Singleton
    internal fun providesStorageDbHelper(context: Context,
                                       @Named("storage db filename") fileName: String?)
            : StorageDbSqliteHelper
            // null filename means in-memory
            // Dagger doesn't allow null values, so pass "" (empty string) instead null
            = StorageDbSqliteHelper(context, if (fileName?.isEmpty() ?: false) null else fileName) // transform "" => null

    @Provides
    @Singleton
    @Named("storage db filename")
    open fun providesStorageDbFileName()
            : String
            = "storage"

}