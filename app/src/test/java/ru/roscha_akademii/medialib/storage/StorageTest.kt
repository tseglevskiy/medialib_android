package ru.roscha_akademii.medialib.storage

import android.app.DownloadManager
import android.database.MatrixCursor
import com.nhaarman.mockito_kotlin.*
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication
import ru.roscha_akademii.medialib.storage.model.StorageDbSqliteHelper
import ru.roscha_akademii.medialib.storage.model.StorageStatus
import ru.roscha_akademii.medialib.storage.model.StorageTable
import ru.roscha_akademii.medialib.storage.model.FileStorageRecord
import ru.roscha_akademii.medialib.video.model.VideoDbModule

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class StorageTest {
    lateinit var storage: StorageImpl // SUT

    lateinit var downloadManager: DownloadManager
    lateinit var dbHelper: StorageDbSqliteHelper
    lateinit var storIo: StorIOSQLite
    lateinit var downloadManagerQueryResult: MatrixCursor

    val inProgressTestRecord = FileStorageRecord(
            remoteUri = "remote_url",
            downloadId = 111,
            status = StorageStatus.PROGRESS,
            localUri = "local_url",
            percent = 50)

    @Before
    fun setUp() {
        downloadManager = mock<DownloadManager>()

        val app = RuntimeEnvironment.application as RobolectricMdiaLibApplication

        val component = DaggerApplicationComponent
                .builder()
                .androidModule(object : AndroidModule(app, app.refWatcher) {
                    override fun providesDownloadManager() = downloadManager
                })
                .videoDbModule(object : VideoDbModule() {
                    override fun providesVideoDbFileName() = "" // in-memory database for tests
                })
                .storageModule(object : StorageModule() {
                    override fun providesStorageDbFileName() = "" // in-memory database for tests
                })
                .build()

        app.setTestComponent(component)

        storage = component.videoStorage() as StorageImpl // SUT

        dbHelper = component.storageDbHelper()
        storIo = component.storageDbStorIo()

        downloadManagerQueryResult = MatrixCursor(arrayOf(
                DownloadManager.COLUMN_STATUS,
                DownloadManager.COLUMN_REASON,
                DownloadManager.COLUMN_LOCAL_URI,
                DownloadManager.COLUMN_TOTAL_SIZE_BYTES,
                DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
        ))
    }

    @Test
    fun testDbInMemory() {
        assertNull("test db not in memory", dbHelper.databaseName)
    }

    @Test
    fun storageTableExisting() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + StorageTable.TABLE_NAME + "';", null)
        assertEquals("table not exists", 1, cursor.count)
        cursor.close()
    }

    @Test
    fun tableIsEmpty() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + StorageTable.TABLE_NAME + ";", null)
        assertEquals("table is not empty", 0, cursor.count)
        cursor.close()
    }

    @Test
    fun getUnexistingRecord() {
        val record: FileStorageRecord? = storage.getRecord("remote_url")

        assertNull(record)
    }

    @Test
    fun getPercentForUnexistingRecord() {
        val percent: Int = storage.getPercent("unknown remote uri")

        assertEquals(0, percent)
    }

    @Test
    fun getStatusForUnexistingRecord() {
        val status: StorageStatus = storage.getStatus("unknown remote uri")

        assertEquals(StorageStatus.REMOTE, status)
    }

    @Test
    fun testDataOneRecord() {
        inProgressTestRecord.saveForTest()

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + StorageTable.TABLE_NAME + ";", null)
        assertEquals("table is not empty", 1, cursor.count)
        cursor.close()
    }

    @Test
    fun getRecordForExistingRecord() {
        inProgressTestRecord.saveForTest()

        val record: FileStorageRecord? = storage.getRecord(inProgressTestRecord.remoteUri)

        assertNotNull(record)
        assertEquals(inProgressTestRecord, record)
    }

    @Test
    fun getPercentForExistingRecord() {
        val testRecord = FileStorageRecord(
                remoteUri = "remote_uri_111",
                downloadId = 111,
                percent = 87)
        testRecord.saveForTest()

        val percent: Int = storage.getPercent(testRecord.remoteUri)

        assertEquals(testRecord.percent, percent)
    }

    @Test
    fun checkDownloadStatusForUnexistingRecord() {
        storage.checkDownloadStatus("unknown uri")

        verifyZeroInteractions(downloadManager)
    }

    /**
     * если запись уже помечена как LOCAL, то ничего не предпринимается:
     * решающим является, доступен ли сохраненный локальный файл.
     */
    @Test
    fun checkDownloadStatusForLocalRecord() {
        val testRecord = FileStorageRecord(
                remoteUri = "remote_uri",
                downloadId = 111,
                status = StorageStatus.LOCAL,
                localUri = "local_url",
                percent = 100)
        testRecord.saveForTest()

        storage.checkDownloadStatus(testRecord.remoteUri)

        verifyZeroInteractions(downloadManager)

        val record = storage.getRecord(testRecord.remoteUri)
        assertNotNull(record)
    }


    /**
     * если DownloadManager не вернул данные о загрузке, удаляем соответствующую запись из
     * базы локального стораджа
     */
    @Test
    fun checkDownloadStatusForProgressRecordIfCancelled() {
        inProgressTestRecord.saveForTest()

        whenever(downloadManager.query(any())).thenReturn(downloadManagerQueryResult)

        storage.checkDownloadStatus(inProgressTestRecord.remoteUri)

        // TODO проверить, что query приходит с нашим downloadId
        // проблема в том, что query - write only объект
        verify(downloadManager, times(1)).query(any())
        assertNull(storage.getRecord(inProgressTestRecord.remoteUri))
    }

    @Test
    fun checkDownloadStatusForProgressRecordIfContinued() {
        inProgressTestRecord.saveForTest()

        downloadManagerQueryResult.addRow(arrayOf(
                DownloadManager.STATUS_RUNNING,
                0, // not used now
                "new_local_uri",
                100000,
                60000
        ))

        whenever(downloadManager.query(any())).thenReturn(downloadManagerQueryResult)

        storage.checkDownloadStatus(inProgressTestRecord.remoteUri)

        verify(downloadManager, times(1)).query(any())

        val record = storage.getRecord(inProgressTestRecord.remoteUri)
        assertNotNull(record)
        assertEquals(inProgressTestRecord.remoteUri, record!!.remoteUri)
        assertEquals(inProgressTestRecord.downloadId, record!!.downloadId)
        assertEquals(StorageStatus.PROGRESS, record!!.status)
        assertEquals(60, record!!.percent)
        assertNull(record!!.localUri)

        val uri = storage.getLocalUriIfAny(inProgressTestRecord.remoteUri)
        assertEquals(inProgressTestRecord.remoteUri, uri)
    }

    @Test
    fun checkDownloadStatusForProgressRecordIfFinished() {
        inProgressTestRecord.saveForTest()

        val colpleteLocalUri = "new_local_uri"

        downloadManagerQueryResult.addRow(arrayOf(
                DownloadManager.STATUS_SUCCESSFUL,
                0, // not used now
                colpleteLocalUri,
                100000,
                60000
        ))

        whenever(downloadManager.query(any())).thenReturn(downloadManagerQueryResult)

        storage.checkDownloadStatus(inProgressTestRecord.remoteUri)

        verify(downloadManager, times(1)).query(any())

        val record = storage.getRecord(inProgressTestRecord.remoteUri)
        assertNotNull(record)
        assertEquals(inProgressTestRecord.remoteUri, record!!.remoteUri)
        assertEquals(inProgressTestRecord.downloadId, record!!.downloadId)
        assertEquals(100, record!!.percent)
        assertEquals(StorageStatus.LOCAL, record!!.status)
        assertEquals("new_local_uri", record!!.localUri)

        val uri = storage.getLocalUriIfAny(inProgressTestRecord.remoteUri)
        assertEquals(colpleteLocalUri, uri)
    }

    /**
     * если DownloadManager сообщил, что загрузка обломалась навсегда, то удаляем
     * запись из локального стораджа
     */
    @Test
    fun checkDownloadStatusForProgressRecordIfFailed() {
        inProgressTestRecord.saveForTest()

        downloadManagerQueryResult.addRow(arrayOf(
                DownloadManager.STATUS_FAILED,
                0, // not used now
                "new_local_uri",
                100000,
                60000
        ))

        whenever(downloadManager.query(any())).thenReturn(downloadManagerQueryResult)

        storage.checkDownloadStatus(inProgressTestRecord.remoteUri)

        verify(downloadManager, times(1)).query(any())

        argumentCaptor<Long>().apply {
            verify(downloadManager, times(1)).remove(capture())
            assertEquals(inProgressTestRecord.downloadId, allValues[0])
        }

        val record = storage.getRecord(inProgressTestRecord.remoteUri)
        assertNull(record)

        val uri = storage.getLocalUriIfAny(inProgressTestRecord.remoteUri)
        assertEquals(inProgressTestRecord.remoteUri, uri)
    }

    /**
     * если запись уже качается, то повторное добавление ни к чему не приводит
     * решающим является, доступен ли сохраненный локальный файл.
     */
    @Test
    fun checkRequestTwiceWhenLocal() {
        val testRecord = FileStorageRecord(
                remoteUri = "remote_uri",
                downloadId = 111,
                status = StorageStatus.LOCAL,
                localUri = "http://example.com/a.data",
                percent = 100)
        testRecord.saveForTest()

        storage.saveLocal(testRecord.remoteUri, "another title", true)

        verifyZeroInteractions(downloadManager)
    }

    /**
     * нужно проверить очистку лишних записей
     */
    @Test
    fun clean() {
        val testRecord1 = FileStorageRecord(
                remoteUri = "remote_uri_1",
                downloadId = 111,
                status = StorageStatus.LOCAL,
                localUri = "http://example.com/a1.data",
                percent = 100)
        testRecord1.saveForTest()

        val testRecord2 = FileStorageRecord(
                remoteUri = "remote_uri_2",
                downloadId = 112,
                status = StorageStatus.LOCAL,
                localUri = "http://example.com/a2.data",
                percent = 100)
        testRecord2.saveForTest()

        val testRecord3 = FileStorageRecord(
                remoteUri = "remote_uri_3",
                downloadId = 113,
                status = StorageStatus.LOCAL,
                localUri = "http://example.com/a3.data",
                percent = 100)
        testRecord3.saveForTest()

        assertNotNull(storage.getRecord(testRecord1.remoteUri))
        assertNotNull(storage.getRecord(testRecord2.remoteUri))
        assertNotNull(storage.getRecord(testRecord3.remoteUri))

        storage.cleanExceptThese(arrayOf(testRecord1.remoteUri, testRecord2.remoteUri).toSet())

        assertNotNull(storage.getRecord(testRecord1.remoteUri))
        assertNotNull(storage.getRecord(testRecord2.remoteUri))
        assertNull(storage.getRecord(testRecord3.remoteUri)) // removed
    }


    //    @Test
//    fun getStatusForExistingRecord() {
//        val testRecord = VideoStorageRecord(
//                id = 1,
//                downloadId = 111,
//                status = StorageStatus.PROGRESS)
//        testRecord.saveForTest()
//
//        val status: StorageStatus = videoStorage.getStatus(testRecord.id)
//
//        assertEquals(testRecord.status, status)
//    }

    private fun FileStorageRecord.saveForTest() {
        storIo.put().`object`(this).prepare().executeAsBlocking()
    }

}