package ru.roscha_akademii.medialib.storage

import org.junit.Assert.*
import org.junit.Test
import ru.roscha_akademii.medialib.storage.model.StorageStatus
import ru.roscha_akademii.medialib.storage.model.FileStorageRecord

class StorageRecordTest() {
    @Test
    fun defaultValues() {
        val record = FileStorageRecord("remote uri", 111)

        assertEquals(StorageStatus.REMOTE, record.status)
        assertEquals(0, record.percent)
        assertNull(record.localUri)
    }
}