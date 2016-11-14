package ru.roscha_akademii.medialib.video.model.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.roscha_akademii.medialib.storage.StorageTable

class StorageDbSqliteHelper internal constructor(context: Context, dbName: String?)
: SQLiteOpenHelper(context, dbName, null, StorageDbSqliteHelper.VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(StorageTable.createTable())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO dummy upgrade
        db.execSQL("drop table if exists " + StorageTable.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private val VERSION = StorageTable.VERSION
    }
}
