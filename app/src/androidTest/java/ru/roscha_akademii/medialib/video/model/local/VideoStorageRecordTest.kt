package ru.roscha_akademii.medialib.video.model.local

import org.junit.Assert.*
import org.junit.Test

class VideoStorageRecordTest() {
    @Test
    fun defaultValues() {
        val record = VideoStorageRecord(1, 111)

        assertEquals(StorageStatus.REMOTE, record.status)
        assertEquals(0, record.percent)
        assertNull(record.localUri)
    }
}