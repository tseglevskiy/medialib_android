package ru.roscha_akademii.medialib.video.model.local

import android.app.DownloadManager
import android.database.MatrixCursor
import android.support.test.InstrumentationRegistry
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.stubbing.OngoingStubbing
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.MockMediaLibApplication
import ru.roscha_akademii.medialib.video.model.VideoDbModule


class VideoStorageTest() {
    lateinit var videoStorage: VideoStorage // SUT

    lateinit var downloadManager: DownloadManager
    lateinit var storIo: StorIOSQLite
    lateinit var downloadManagerQueryResult: MatrixCursor

    val inProgressTestRecord = VideoStorageRecord(
            id = 1,
            downloadId = 111,
            status = StorageStatus.PROGRESS,
            localUri = "local_url",
            percent = 50)

    @Before
    fun setUp() {
        downloadManager = mock(DownloadManager::class.java)

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

        videoStorage = component.videoStorage()
        storIo = component.videoDbStorIo()

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
        val record: VideoStorageRecord? = videoStorage.getRecord(111111)

        assertNull(record)
    }

    @Test
    fun getPercentForUnexistingRecord() {
        val percent: Int = videoStorage.getPercent(1111111)

        assertEquals(0, percent)
    }

    @Test
    fun getStatusForUnexistingRecord() {
        val status: StorageStatus = videoStorage.getStatus(1111111)

        assertEquals(StorageStatus.REMOTE, status)
    }

    @Test
    fun getRecordForExistingRecord() {
        inProgressTestRecord.saveForTest()

        val record: VideoStorageRecord? = videoStorage.getRecord(inProgressTestRecord.id)

        assertNotNull(record)
        assertEquals(inProgressTestRecord, record)
    }

    @Test
    fun getPercentForExistingRecord() {
        val testRecord = VideoStorageRecord(
                id = 1,
                downloadId = 111,
                percent = 87)
        testRecord.saveForTest()

        val percent: Int = videoStorage.getPercent(testRecord.id)

        assertEquals(testRecord.percent, percent)
    }

    @Test
    fun checkDownloadStatusForUnexistingRecord() {
        videoStorage.checkDownloadStatus(11111111)

        verifyZeroInteractions(downloadManager)
    }

    /**
     * если запись уже помечена как LOCAL, то ничего не предпринимается:
     * решающим является, доступен ли сохраненный локальный файл.
     */
    @Test
    fun checkDownloadStatusForLocalRecord() {
        val testRecord = VideoStorageRecord(
                id = 1,
                downloadId = 111,
                status = StorageStatus.LOCAL,
                localUri = "local_url",
                percent = 100)
        testRecord.saveForTest()

        videoStorage.checkDownloadStatus(testRecord.id)

        verifyZeroInteractions(downloadManager)
    }

    /**
     * если DownloadManager не вернул данные о загрузке, удаляем соответствующую запись из
     * базы локального стораджа
     */
    @Test
    fun checkDownloadStatusForProgressRecordIfCancelled() {
        inProgressTestRecord.saveForTest()

        whenever(downloadManager.query(any())).thenReturn(downloadManagerQueryResult)

        videoStorage.checkDownloadStatus(inProgressTestRecord.id)

        // TODO проверить, что query приходит с нашим downloadId
        // проблема в том, что query - write only объект
        verify(downloadManager, times(1)).query(any())
        assertNull(videoStorage.getRecord(inProgressTestRecord.id))
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

        videoStorage.checkDownloadStatus(inProgressTestRecord.id)

        verify(downloadManager, times(1)).query(any())

        val record = videoStorage.getRecord(inProgressTestRecord.id)
        assertNotNull(record)
        assertEquals(inProgressTestRecord.id, record?.id)
        assertEquals(inProgressTestRecord.downloadId, record?.downloadId)
        assertEquals(StorageStatus.PROGRESS, record?.status)
        assertEquals(60, record?.percent)
        assertNull(record?.localUri)
    }

    @Test
    fun checkDownloadStatusForProgressRecordIfFinished() {
        inProgressTestRecord.saveForTest()

        downloadManagerQueryResult.addRow(arrayOf(
                DownloadManager.STATUS_SUCCESSFUL,
                0, // not used now
                "new_local_uri",
                100000,
                60000
        ))

        whenever(downloadManager.query(any())).thenReturn(downloadManagerQueryResult)

        videoStorage.checkDownloadStatus(inProgressTestRecord.id)

        verify(downloadManager, times(1)).query(any())

        val record = videoStorage.getRecord(inProgressTestRecord.id)
        assertNotNull(record)
        assertEquals(inProgressTestRecord.id, record?.id)
        assertEquals(inProgressTestRecord.downloadId, record?.downloadId)
        assertEquals(StorageStatus.LOCAL, record?.status)
        assertEquals(100, record?.percent)
        assertEquals("new_local_uri", record?.localUri)
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

        videoStorage.checkDownloadStatus(inProgressTestRecord.id)

        verify(downloadManager, times(1)).query(any())

        val record = videoStorage.getRecord(inProgressTestRecord.id)
        assertNull(record)
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

    fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)!!

}