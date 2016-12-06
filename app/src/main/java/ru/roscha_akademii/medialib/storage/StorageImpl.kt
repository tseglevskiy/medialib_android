package ru.roscha_akademii.medialib.storage

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio.sqlite.queries.Query
import java.io.FileNotFoundException

open class StorageImpl(internal val db: StorIOSQLite,
                       val context: Context,
                       val contentResolver: ContentResolver,
                       val downloadManager: DownloadManager) : Storage {

    override fun getStatus(remoteUri: String): StorageStatus {
        checkDownloadStatus(remoteUri)
        checkLocalUri(remoteUri)

        val rerord = getRecord(remoteUri)
        Log.d("happy", rerord.toString())
        return rerord?.status ?: StorageStatus.REMOTE
    }

    override fun checkDownloadStatus(remoteUri: String) {
        val record = getRecord(remoteUri) ?: return
        checkDownloadStatus(record)
    }

    override fun checkDownloadStatus(record: VideoStorageRecord) {
        if (record.status == StorageStatus.LOCAL) return

        val downloadId = record.downloadId
        val query = DownloadManager.Query()
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
            else -> removeLocal(record.remoteUri)
        }

    }

    override fun checkLocalUri(remoteUri: String) {
        val record = getRecord(remoteUri) ?: return
        val uriStr = record.localUri ?: return

        try {
            val inps = contentResolver.openInputStream(Uri.parse(uriStr))
            inps.close()
        } catch (e: FileNotFoundException) {
            removeLocal(remoteUri)
        }
    }

    override fun getPercent(remoteUri: String): Int {
        return getRecord(remoteUri)?.percent?.toInt() ?: return 0
    }

    override fun saveLocal(remoteUri: String, title: String, visible: Boolean) {
        checkDownloadStatus(remoteUri)
        if (getRecord(remoteUri) != null) return

        val uri = Uri.parse(remoteUri)

        val request = DownloadManager.Request(uri)
        request.setTitle(title)
//        request.setDescription("description description description")
        request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_NOTIFICATIONS,
                remoteUri.hashCode().toString())
        request.setVisibleInDownloadsUi(false)
        request.setNotificationVisibility(
                if (visible) DownloadManager.Request.VISIBILITY_VISIBLE
                else DownloadManager.Request.VISIBILITY_HIDDEN)

        val downloadRef = downloadManager.enqueue(request)
        VideoStorageRecord(remoteUri, downloadRef).save()

        checkDownloadStatus(remoteUri)
    }

    override fun removeLocal(remoteUri: String) {
        val record = getRecord(remoteUri) ?: return

        downloadManager.remove(record.downloadId)

        record.delete()
    }

    private fun VideoStorageRecord.save() {
        db.put().`object`(this).prepare().executeAsBlocking()
    }

    private fun VideoStorageRecord.delete() {
        db.delete().`object`(this).prepare().executeAsBlocking()
    }

    fun getRecord(remoteUri: String): VideoStorageRecord? {
        return try {
            db.get()
                    .listOfObjects(VideoStorageRecord::class.java)
                    .withQuery(Query.builder()
                            .table(StorageTable.TABLE_NAME)
                            .where(StorageTable.REMOTE_URI + " = ?")
                            .whereArgs(remoteUri)
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

    override fun getLocalUriIfAny(remoteUri: String): String {
        return getRecord(remoteUri)?.localUri ?: remoteUri
    }


    override fun cleanExceptThese(alive: Set<String>) {
        val cursor = db.get()
                .cursor()
                .withQuery(Query.builder()
                        .table(StorageTable.TABLE_NAME)
                        .columns(StorageTable.REMOTE_URI)
                        .build())
                .prepare()
                .executeAsBlocking()

        try {
            if (cursor.moveToFirst()) {
                do {
                    val url = cursor.getString(0)
                    if (!alive.contains(url)) {
                        db.delete()
                                .byQuery(DeleteQuery.builder()
                                        .table(StorageTable.TABLE_NAME)
                                        .where(StorageTable.REMOTE_URI + "=?")
                                        .whereArgs(url)
                                        .build())
                                .prepare()
                                .executeAsBlocking()

                    }
                } while (cursor.moveToNext())
            }

        } finally {
            cursor.close()
        }

    }
}

