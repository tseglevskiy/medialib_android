package ru.roscha_akademii.medialib.video.model.local

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING


object VideoTable {
    val VERSION = 6

    val TABLE_NAME = "video"

    val ID = "_id"
    val TITLE = "title"
    val DESCRIPTION = "description"
    val PICTURE_URL = "picture_url"
    val VIDEO_URL = "video_url"
    val DURATION = "duration"
    val DATE = "date"

    fun createTable(): String {
        return CreateTableQueryBuilder(TABLE_NAME)
                .index(ID)
                .column(TITLE, STRING)
                .column(DESCRIPTION, STRING)
                .column(PICTURE_URL, STRING)
                .column(VIDEO_URL, STRING)
                .column(DURATION, STRING)
                .column(DATE, STRING)
                .build()
    }
}
