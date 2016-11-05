package ru.roscha_akademii.medialib.video.model.local

import org.junit.Assert.assertTrue
import org.junit.Test

class VideoStorageTableTest() {

    @Test
    fun createVideoTable() {
        val query = VideoStorageTable.createTable()
        assertTrue(query.startsWith("create table ${VideoStorageTable.TABLE_NAME} "))
        assertTrue(query.contains("${VideoStorageTable.ID} INTEGER PRIMARY KEY"))
        assertTrue(query.contains("${VideoStorageTable.STATUS} INTEGER"))
        assertTrue(query.contains("${VideoStorageTable.LOCAL_URI} STRING"))
        assertTrue(query.contains("${VideoStorageTable.DOWNLOAD_ID} INTEGER"))
        assertTrue(query.contains("${VideoStorageTable.PERCENT} INTEGER"))
    }

}