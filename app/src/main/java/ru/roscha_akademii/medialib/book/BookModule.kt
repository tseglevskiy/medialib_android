package ru.roscha_akademii.medialib.book

import android.content.Context
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.roscha_akademii.medialib.book.model.local.BookDb
import ru.roscha_akademii.medialib.book.model.local.BookDbSqliteHelper
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile
import ru.roscha_akademii.medialib.book.model.local.storio.BookFileMapping
import ru.roscha_akademii.medialib.book.model.local.storio.BookMapping
import ru.roscha_akademii.medialib.book.model.remote.BookApi
import ru.roscha_akademii.medialib.book.model.remote.BookUpdate
import ru.roscha_akademii.medialib.book.model.local.entity.Book
import ru.roscha_akademii.medialib.storage.Storage

import javax.inject.Named
import javax.inject.Singleton

@Module
open class BookModule {
    @Provides
    @Singleton
    @Named("book db filename")
    open fun providesBookDbFileName()
            : String
            = "book"

    @Provides
    @Singleton
    internal fun providesBookDbHelper(context: Context,
                                      @Named("book db filename") fileName: String?)
            : BookDbSqliteHelper
            // null filename means in-memory
            // Dagger doesn't allow null values, so pass "" (empty string) instead null
            = BookDbSqliteHelper(context, if (fileName?.isEmpty() ?: false) null else fileName) // transform "" => null

    @Provides
    @Singleton
    @Named("book db")
    internal fun providesBookDbSio(dbHelper: BookDbSqliteHelper)
            : StorIOSQLite
            = DefaultStorIOSQLite
            .builder()
            .sqliteOpenHelper(dbHelper)
            .addTypeMapping(Book::class.java, BookMapping())
            .addTypeMapping(BookFile::class.java, BookFileMapping())
            .build()

    @Provides
    @Singleton
    internal fun providesBookDb(@Named("book db") storio: StorIOSQLite)
            : BookDb
            = BookDb(storio)

    @Provides
    @Singleton
    internal fun providesBookApi(retrofit: Retrofit)
            : BookApi
            = retrofit.create(BookApi::class.java)

    @Provides
    @Singleton
    open internal fun providesBookUpdate(api: BookApi, bookDb: BookDb, storage: Storage)
            : BookUpdate
            = BookUpdate(api, bookDb, storage)

}


