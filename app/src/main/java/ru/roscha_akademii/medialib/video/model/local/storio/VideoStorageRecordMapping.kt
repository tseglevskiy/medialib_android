package ru.roscha_akademii.medialib.video.model.local.storio

import android.content.ContentValues
import android.database.Cursor
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio.sqlite.queries.InsertQuery
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery

import ru.roscha_akademii.medialib.video.model.local.*

class VideoStorageRecordMapping : SQLiteTypeMapping<VideoStorageRecord>(
        PutResolver(),
        GetResolver(),
        DeleteResolver()) {

    class PutResolver : DefaultPutResolver<VideoStorageRecord>() {
        public override fun mapToInsertQuery(obj: VideoStorageRecord): InsertQuery {
            return InsertQuery.builder()
                    .table(VideoStorageTable.TABLE_NAME)
                    .build()
        }

        public override fun mapToUpdateQuery(obj: VideoStorageRecord): UpdateQuery {
            return UpdateQuery.builder()
                    .table(VideoStorageTable.TABLE_NAME)
                    .where(VideoStorageTable.ID + " = ?")
                    .whereArgs(obj.id)
                    .build()
        }

        public override fun mapToContentValues(obj: VideoStorageRecord): ContentValues {
            val contentValues = ContentValues()

            contentValues.put(VideoStorageTable.LOCAL_URI, obj.localUri)
            contentValues.put(VideoStorageTable.ID, obj.id)
            contentValues.put(VideoStorageTable.DOWNLOAD_ID, obj.downloadId)
            contentValues.put(VideoStorageTable.PERCENT, obj.percent)
            contentValues.put(VideoStorageTable.STATUS, obj.status.value)

            return contentValues
        }
    }

    class GetResolver : DefaultGetResolver<VideoStorageRecord>() {
        override fun mapFromCursor(cursor: Cursor): VideoStorageRecord {
            return VideoStorageRecord(
                    id = cursor.getLong(cursor.getColumnIndex(VideoStorageTable.ID)),
                    downloadId = cursor.getLong(cursor.getColumnIndex(VideoStorageTable.DOWNLOAD_ID)),
                    localUri = cursor.getString(cursor.getColumnIndex(VideoStorageTable.LOCAL_URI)),
                    status = StorageStatus.fromInt(
                            cursor.getInt(cursor.getColumnIndex(VideoStorageTable.STATUS))),
                    percent = cursor.getInt(cursor.getColumnIndex(VideoStorageTable.PERCENT))
            )
        }
    }

    class DeleteResolver : DefaultDeleteResolver<VideoStorageRecord>() {
        public override fun mapToDeleteQuery(`object`: VideoStorageRecord): DeleteQuery {
            return DeleteQuery.builder()
                    .table(VideoStorageTable.TABLE_NAME)
                    .where(VideoStorageTable.ID + " = ?")
                    .whereArgs(`object`.id)
                    .build()
        }
    }
}
