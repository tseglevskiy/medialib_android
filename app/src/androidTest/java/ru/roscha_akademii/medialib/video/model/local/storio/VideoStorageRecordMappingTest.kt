package ru.roscha_akademii.medialib.video.model.local.storio

import android.database.MatrixCursor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.roscha_akademii.medialib.video.model.local.StorageStatus
import ru.roscha_akademii.medialib.video.model.local.VideoStorageRecord
import ru.roscha_akademii.medialib.video.model.local.VideoStorageTable

class VideoStorageRecordMappingTest() {
    lateinit var mapping: VideoStorageRecordMapping
    lateinit var putResolver: VideoStorageRecordMapping.PutResolver
    lateinit var getResolver: VideoStorageRecordMapping.GetResolver
    lateinit var deleteResolver: VideoStorageRecordMapping.DeleteResolver

    @Before
    fun setUp() {
        mapping = VideoStorageRecordMapping()
        putResolver = mapping.putResolver() as VideoStorageRecordMapping.PutResolver
        getResolver = mapping.getResolver() as VideoStorageRecordMapping.GetResolver
        deleteResolver = mapping.deleteResolver() as VideoStorageRecordMapping.DeleteResolver
    }

    val record1 = VideoStorageRecord(
            id = 1111,
            downloadId = 2222,
            localUri = "local url",
            status = StorageStatus.PROGRESS,
            percent = 49
    )

    @Test
    fun mapToUpdateQuery_identifyVideoById() {
        val query = putResolver.mapToUpdateQuery(record1)

        assertEquals(VideoStorageTable.TABLE_NAME, query.table())

        assertEquals("${VideoStorageTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(record1.id, query.whereArgs()[0].toLong())
    }

    @Test
    fun mapToContentValues() {
        val values = putResolver.mapToContentValues(record1)

        assertEquals(5, values.size())
        assertEquals(record1.id, values.get(VideoStorageTable.ID))
        assertEquals(record1.downloadId, values.get(VideoStorageTable.DOWNLOAD_ID))
        assertEquals(record1.localUri, values.get(VideoStorageTable.LOCAL_URI))
        assertEquals(record1.status.value, values.get(VideoStorageTable.STATUS))
        assertEquals(record1.percent, values.get(VideoStorageTable.PERCENT))
    }

    @Test
    fun mapToInsertQuery() {
        val query = putResolver.mapToInsertQuery(record1)

        assertEquals(VideoStorageTable.TABLE_NAME, query.table())
    }

    @Test
    fun mapFromCursor() {
        val cursor = MatrixCursor(arrayOf(
                VideoStorageTable.ID,
                VideoStorageTable.STATUS,
                VideoStorageTable.LOCAL_URI,
                VideoStorageTable.PERCENT,
                VideoStorageTable.DOWNLOAD_ID
        ))

        cursor.newRow()
                .add(record1.id)
                .add(record1.status.value)
                .add(record1.localUri)
                .add(record1.percent)
                .add(record1.downloadId)

        cursor.moveToFirst()

        val record = getResolver.mapFromCursor(cursor)

        assert(record1.equals(record))
    }

    @Test
    fun mapToDeleteQuery() {
        val query = deleteResolver.mapToDeleteQuery(record1)

        assertEquals(VideoStorageTable.TABLE_NAME, query.table())

        assertEquals("${VideoStorageTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(record1.id, query.whereArgs()[0].toLong())
    }
}