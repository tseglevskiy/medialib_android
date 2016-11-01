package ru.roscha_akademii.medialib.video.model.local

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.Query
import java.io.FileNotFoundException

class VideoStorage(internal val db: StorIOSQLite,
                   val videoDb: VideoDb,
                   val context: Context,
                   val contentResolver: ContentResolver,
                   val downloadManager: DownloadManager) {

    fun getStatus(id: Long): StorageStatus {
        checkDownloadStatus(id)
        checkLocalUri(id)

        val rerord = getRecord(id)
        Log.d("happy", rerord.toString())
        return rerord?.status ?: StorageStatus.REMOTE
    }

    fun checkDownloadStatus(id: Long) {
        val record = getRecord(id) ?: return
        checkDownloadStatus(record)
    }
    fun checkDownloadStatus(record: VideoStorageRecord) {
        if (record.status == StorageStatus.LOCAL) return

        val query = DownloadManager.Query()
        val downloadId = record.downloadId
        query.setFilterById(downloadId)
        val cursor = downloadManager.query(query)

        if (!cursor.moveToFirst()) {
            record.delete()
            return
        }

        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
//        val reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
        val fileUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
        val totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
        val downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
        val percent = if (totalBytes > 10000) 100.times(downloadedBytes).div(totalBytes).toInt() else 0

        cursor.close()

        when (status) {
            DownloadManager.STATUS_SUCCESSFUL -> {
                record.status = StorageStatus.LOCAL
                record.localUri = fileUri
                record.percent = 100
                record.save()
            }

            DownloadManager.STATUS_PENDING -> {
                record.status = StorageStatus.PROGRESS
                record.localUri = null
                record.percent = 0
                record.save()
            }

            DownloadManager.STATUS_PAUSED, DownloadManager.STATUS_RUNNING -> {
                record.status = StorageStatus.PROGRESS
                record.localUri = null
                record.percent = percent
                record.save()
            }

        // DownloadManager.STATUS_FAILED
            else -> removeLocal(record.id)
        }

    }

    fun checkLocalUri(id: Long) {
        val record = getRecord(id) ?: return
        val uriStr = record.localUri ?: return

        try {
            val inps = contentResolver.openInputStream(Uri.parse(uriStr))
            inps.close()
        } catch (e: FileNotFoundException) {
            removeLocal(id)
        }
    }

    fun getPercent(id: Long): Int {
        return getRecord(id)?.percent?.toInt() ?: return 0
    }

    fun saveLocal(id: Long) {
        checkDownloadStatus(id)
        if (getRecord(id) != null) return

        val video = videoDb.getVideo(id)
        val uri = Uri.parse(video.videoUrl)

        val request = DownloadManager.Request(uri)
        request.setTitle(video.title)
//        request.setDescription("description description description")
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_NOTIFICATIONS, "video_$id.mp4")
//        request.setVisibleInDownloadsUi(false)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        val downloadRef = downloadManager.enqueue(request)
        VideoStorageRecord(id, downloadRef).save()

        checkDownloadStatus(id)
    }

    fun removeLocal(videoId: Long) {
        checkDownloadStatus(videoId)
        val record = getRecord(videoId) ?: return

        downloadManager.remove(record.downloadId)

        record.delete()
    }

    private fun VideoStorageRecord.save() {
        db.put().`object`(this).prepare().executeAsBlocking()
    }

    private fun VideoStorageRecord.delete() {
        db.delete().`object`(this).prepare().executeAsBlocking()
    }

    fun getRecord(id: Long): VideoStorageRecord? {
        return try {
            db.get()
                    .listOfObjects(VideoStorageRecord::class.java)
                    .withQuery(Query.builder()
                            .table(VideoStorageTable.TABLE_NAME)
                            .where(VideoStorageTable.ID + " = ?")
                            .whereArgs(id)
                            .orderBy(VideoStorageTable.ID)
                            .build())
                    .prepare()
                    .executeAsBlocking()[0]
        } catch (e: IndexOutOfBoundsException) {
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

