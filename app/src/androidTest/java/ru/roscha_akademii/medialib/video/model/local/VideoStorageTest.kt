package ru.roscha_akademii.medialib.video.model.local

import android.app.DownloadManager
import android.database.MatrixCursor
import android.support.test.InstrumentationRegistry
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Matchers.any
import ru.roscha_akademii.medialib.common.AndroidModule
import ru.roscha_akademii.medialib.common.DaggerApplicationComponent
import ru.roscha_akademii.medialib.common.MockMediaLibApplication
import ru.roscha_akademii.medialib.video.model.VideoDbModule
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.stubbing.OngoingStubbing


class VideoStorageTest() {
    lateinit var videoStorage: VideoStorage // SUT

    lateinit var downloadManager: DownloadManager
    lateinit var storIo: StorIOSQLite

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
        val testRecord = VideoStorageRecord(
                id = 1,
                downloadId = 111)
        testRecord.saveForTest()

        val record: VideoStorageRecord? = videoStorage.getRecord(testRecord.id)

        assertNotNull(record)
        assertEquals(testRecord, record)
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

    @Test
    fun checkDownloadStatusForProgressRecord() {
        val testRecord = VideoStorageRecord(
                id = 1,
                downloadId = 111,
                status = StorageStatus.LOCAL,
                localUri = "local_url",
                percent = 100)
        testRecord.saveForTest()

        val downloadManagerQueryResult = MatrixCursor(arrayOf(
                DownloadManager.COLUMN_STATUS,
                DownloadManager.COLUMN_REASON,
                DownloadManager.COLUMN_LOCAL_URI,
                DownloadManager.COLUMN_TOTAL_SIZE_BYTES,
                DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
        ))

        whenever(downloadManager.query(any())).thenReturn(downloadManagerQueryResult)

        videoStorage.checkDownloadStatus(testRecord.id)

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

    fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)!!

}