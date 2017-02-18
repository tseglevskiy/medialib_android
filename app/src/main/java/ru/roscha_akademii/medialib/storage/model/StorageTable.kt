package ru.roscha_akademii.medialib.storage.model

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.STRING
import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.INTEGER

object StorageTable {
    val VERSION = 6

    val TABLE_NAME = "video_storage"

    val REMOTE_URI = "remote_uri"
    val STATUS = "status"
    val LOCAL_URI = "local_uri"
    val DOWNLOAD_ID = "download_id"
    val PERCENT = "percent"

    fun createTable(): String {
        return CreateTableQueryBuilder(TABLE_NAME)
                .index(REMOTE_URI, STRING)
                .column(STATUS, INTEGER)
                .column(LOCAL_URI, STRING)
                .column(DOWNLOAD_ID, INTEGER)
                .column(PERCENT, INTEGER)
                .build()
    }
}
