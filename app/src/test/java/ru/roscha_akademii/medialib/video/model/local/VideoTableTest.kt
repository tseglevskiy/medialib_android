package ru.roscha_akademii.medialib.video.model.local

import org.junit.Assert.assertTrue
import org.junit.Test

class VideoTableTest {
    @Test
    fun createVideoTable() {
        val query = VideoTable.createTable()
        assertTrue(query.startsWith("create table ${VideoTable.TABLE_NAME} "))
        assertTrue(query.contains("${VideoTable.ID} INTEGER PRIMARY KEY"))
        assertTrue(query.contains("${VideoTable.TITLE} STRING"))
        assertTrue(query.contains("${VideoTable.DESCRIPTION} STRING"))
        assertTrue(query.contains("${VideoTable.VIDEO_URL} STRING"))
        assertTrue(query.contains("${VideoTable.PICTURE_URL} STRING"))
    }

}