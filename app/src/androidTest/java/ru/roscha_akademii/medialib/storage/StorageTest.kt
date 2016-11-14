package ru.roscha_akademii.medialib.storage

import android.app.DownloadManager
import android.database.MatrixCursor
import android.support.test.InstrumentationRegistry
import com.nhaarman.mockito_kotlin.*
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.MockMediaLibApplication
import ru.roscha_akademii.medialib.video.model.VideoDbModule
import ru.roscha_akademii.medialib.whenever


class StorageTest() {
    lateinit var storage: StorageImpl // SUT

    lateinit var downloadManager: DownloadManager
    lateinit var storIo: StorIOSQLite
    lateinit var downloadManagerQueryResult: MatrixCursor

    val inProgressTestRecord = VideoStorageRecord(
            remoteUri = "remote_url",
            downloadId = 111,
            status = StorageStatus.PROGRESS,
            localUri = "local_url",
            percent = 50)

    @Before
    fun setUp() {
        downloadManager = mock<DownloadManager>()

        val instrumentation = InstrumentationRegistry.getInstrumentation()

        val app = instrumentation.targetContext.applicationContext as MockMediaLibApplication

        val component = DaggerApplicationComponent
                .builder()
                .androidModule(object : AndroidModule(app, app.refWatcher) {
                    override fun providesDownloadManager() = downloadManager
                })
                .videoDbModule(object : VideoDbModule() {
                    override fun providesVideoDbFileName() = "" // in-memory database for tests
                })
                .build()

        app.setTestComponent(component)

        storage = component.videoStorage() as StorageImpl
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
    fun getUnexistingRecord() {
        val record: VideoStorageRecord? = storage.getRecord("remote_url")

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
    fun getRecordForExistingRecord() {
        inProgressTestRecord.saveForTest()

        val record: VideoStorageRecord? = storage.getRecord(inProgressTestRecord.remoteUri)

        assertNotNull(record)
        assertEquals(inProgressTestRecord, record)
    }

    @Test
    fun getPercentForExistingRecord() {
        val testRecord = VideoStorageRecord(
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
        val testRecord = VideoStorageRecord(
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
        val testRecord = VideoStorageRecord(
                remoteUri = "remote_uri",
                downloadId = 111,
                status = StorageStatus.LOCAL,
                localUri = "http://example.com/a.data",
                percent = 100)
        testRecord.saveForTest()

        storage.saveLocal(testRecord.remoteUri, "another title", true)

        verifyZeroInteractions(downloadManager)
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

    private fun VideoStorageRecord.saveForTest() {
        storIo.put().`object`(this).prepare().executeAsBlocking()
    }

}