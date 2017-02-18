package ru.roscha_akademii.medialib.storage.model

import android.database.MatrixCursor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import ru.roscha_akademii.medialib.storage.model.StorageStatus

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")
class StorageRecordMappingTest() {
    lateinit var mapping: StorageRecordMapping
    lateinit var putResolver: StorageRecordMapping.PutResolver
    lateinit var getResolver: StorageRecordMapping.GetResolver
    lateinit var deleteResolver: StorageRecordMapping.DeleteResolver

    @Before
    fun setUp() {
        mapping = StorageRecordMapping()
        putResolver = mapping.putResolver() as StorageRecordMapping.PutResolver
        getResolver = mapping.getResolver() as StorageRecordMapping.GetResolver
        deleteResolver = mapping.deleteResolver() as StorageRecordMapping.DeleteResolver
    }

    val record1 = FileStorageRecord(
            remoteUri = "remote_url",
            downloadId = 2222,
            localUri = "local url",
            status = StorageStatus.PROGRESS,
            percent = 49
    )

    @Test
    fun mapToUpdateQuery_identifyVideoById() {
        val query = putResolver.mapToUpdateQuery(record1)

        assertEquals(StorageTable.TABLE_NAME, query.table())

        assertEquals("${StorageTable.REMOTE_URI}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(record1.remoteUri, query.whereArgs()[0])
    }

    @Test
    fun mapToContentValues() {
        val values = putResolver.mapToContentValues(record1)

        assertEquals(5, values.size())
        assertEquals(record1.remoteUri, values.get(StorageTable.REMOTE_URI))
        assertEquals(record1.downloadId, values.get(StorageTable.DOWNLOAD_ID))
        assertEquals(record1.localUri, values.get(StorageTable.LOCAL_URI))
        assertEquals(record1.status.value, values.get(StorageTable.STATUS))
        assertEquals(record1.percent, values.get(StorageTable.PERCENT))
    }

    @Test
    fun mapToInsertQuery() {
        val query = putResolver.mapToInsertQuery(record1)

        assertEquals(StorageTable.TABLE_NAME, query.table())
    }

    @Test
    fun mapFromCursor() {
        val cursor = MatrixCursor(arrayOf(
                StorageTable.REMOTE_URI,
                StorageTable.STATUS,
                StorageTable.LOCAL_URI,
                StorageTable.PERCENT,
                StorageTable.DOWNLOAD_ID
        ))

        cursor.newRow()
                .add(record1.remoteUri)
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

        assertEquals(StorageTable.TABLE_NAME, query.table())

        assertEquals("${StorageTable.REMOTE_URI}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(record1.remoteUri, query.whereArgs()[0])
    }
}