package ru.roscha_akademii.medialib.storage

import org.junit.Assert.assertTrue
import org.junit.Test
import ru.roscha_akademii.medialib.storage.model.StorageTable

class StorageTableTest() {

    @Test
    fun createVideoTable() {
        val query = StorageTable.createTable()
        assertTrue(query.startsWith("create table ${StorageTable.TABLE_NAME} "))
        assertTrue(query.contains("${StorageTable.REMOTE_URI} STRING PRIMARY KEY"))
        assertTrue(query.contains("${StorageTable.STATUS} INTEGER"))
        assertTrue(query.contains("${StorageTable.LOCAL_URI} STRING"))
        assertTrue(query.contains("${StorageTable.DOWNLOAD_ID} INTEGER"))
        assertTrue(query.contains("${StorageTable.PERCENT} INTEGER"))
    }

}