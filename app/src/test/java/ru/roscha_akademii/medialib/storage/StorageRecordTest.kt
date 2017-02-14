package ru.roscha_akademii.medialib.storage

import org.junit.Assert.*
import org.junit.Test
import ru.roscha_akademii.medialib.storage.StorageStatus
import ru.roscha_akademii.medialib.storage.VideoStorageRecord

class StorageRecordTest() {
    @Test
    fun defaultValues() {
        val record = VideoStorageRecord("remote uri", 111)

        assertEquals(StorageStatus.REMOTE, record.status)
        assertEquals(0, record.percent)
        assertNull(record.localUri)
    }
}