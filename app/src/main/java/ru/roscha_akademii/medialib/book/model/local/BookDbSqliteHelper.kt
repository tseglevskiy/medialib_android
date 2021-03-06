package ru.roscha_akademii.medialib.book.model.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookDbSqliteHelper internal constructor(context: Context, dbName: String?)
: SQLiteOpenHelper(context, dbName, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(BookTable.createTable())

        db.execSQL(BookFileTable.createTable())
        db.execSQL(BookFileTable.createIndex())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO dummy upgrade
        db.execSQL("drop table if exists " + BookTable.TABLE_NAME)

        db.execSQL("drop index if exists " + BookFileTable.INDEX_NAME)
        db.execSQL("drop table if exists " + BookFileTable.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private val VERSION =
                BookTable.VERSION +
                BookFileTable.VERSION
    }
}
