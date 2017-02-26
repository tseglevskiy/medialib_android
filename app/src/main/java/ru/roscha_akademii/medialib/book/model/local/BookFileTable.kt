package ru.roscha_akademii.medialib.book.model.local

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.INTEGER
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING

object BookFileTable {
    val VERSION = 1

    val TABLE_NAME = "book_file"

    val ID = "_id"
    val BOOK = "fook"
    val URL = "url"

    fun createTable(): String {
        return CreateTableQueryBuilder(TABLE_NAME)
                .index(ID)
                .column(BOOK, INTEGER)
                .column(URL, STRING)
                .build()
    }
}
