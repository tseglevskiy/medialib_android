package ru.roscha_akademii.medialib.book.model.local

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.INTEGER
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING

object BookFileTable {
    val VERSION = 2

    val TABLE_NAME = "book_file"

    val BOOK_ID = "book"
    val URL = "url"

    val INDEX_NAME = "book_and_url"

    fun createTable(): String {
        return CreateTableQueryBuilder(TABLE_NAME)
                .column(BOOK_ID, INTEGER)
                .column(URL, STRING)
                .build()
    }

    fun createIndex(): String {
        return "CREATE UNIQUE INDEX IF NOT EXISTS $INDEX_NAME on $TABLE_NAME ($BOOK_ID, $URL)"
    }
}
