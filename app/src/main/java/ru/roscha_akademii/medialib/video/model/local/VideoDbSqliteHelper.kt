package ru.roscha_akademii.medialib.video.model.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VideoDbSqliteHelper internal constructor(context: Context, dbName: String?)
: SQLiteOpenHelper(context, dbName, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(VideoTable.createTable())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO dummy upgrade
        db.execSQL("drop table if exists " + VideoTable.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private val VERSION = VideoTable.VERSION
    }
}
