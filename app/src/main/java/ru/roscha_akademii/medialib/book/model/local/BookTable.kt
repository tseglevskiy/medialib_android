package ru.roscha_akademii.medialib.book.model.local

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING

object BookTable {
    val VERSION = 1

    val TABLE_NAME = "book"

    val ID = "_id"
    val TITLE = "title"
    val DESCRIPTION = "description"
    val PICTURE_URL = "picture_url"

    fun createTable(): String {
        return CreateTableQueryBuilder(TABLE_NAME)
                .index(ID)
                .column(TITLE, STRING)
                .column(DESCRIPTION, STRING)
                .column(PICTURE_URL, STRING)
                .build()
    }
}
