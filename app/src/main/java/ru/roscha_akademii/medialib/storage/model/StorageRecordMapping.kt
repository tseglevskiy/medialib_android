package ru.roscha_akademii.medialib.storage.model

import android.content.ContentValues
import android.database.Cursor
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio.sqlite.queries.InsertQuery
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery
import ru.roscha_akademii.medialib.storage.model.FileStorageRecord
import ru.roscha_akademii.medialib.storage.model.StorageTable

class StorageRecordMapping : SQLiteTypeMapping<FileStorageRecord>(
        PutResolver(),
        GetResolver(),
        DeleteResolver()) {

    class PutResolver : DefaultPutResolver<FileStorageRecord>() {
        public override fun mapToInsertQuery(obj: FileStorageRecord): InsertQuery {
            return InsertQuery.builder()
                    .table(StorageTable.TABLE_NAME)
                    .build()
        }

        public override fun mapToUpdateQuery(obj: FileStorageRecord): UpdateQuery {
            return UpdateQuery.builder()
                    .table(StorageTable.TABLE_NAME)
                    .where(StorageTable.REMOTE_URI + " = ?")
                    .whereArgs(obj.remoteUri)
                    .build()
        }

        public override fun mapToContentValues(obj: FileStorageRecord): ContentValues {
            val contentValues = ContentValues()

            contentValues.put(StorageTable.LOCAL_URI, obj.localUri)
            contentValues.put(StorageTable.REMOTE_URI, obj.remoteUri)
            contentValues.put(StorageTable.DOWNLOAD_ID, obj.downloadId)
            contentValues.put(StorageTable.PERCENT, obj.percent)
            contentValues.put(StorageTable.STATUS, obj.status.value)

            return contentValues
        }
    }

    class GetResolver : DefaultGetResolver<FileStorageRecord>() {
        override fun mapFromCursor(cursor: Cursor): FileStorageRecord {
            return FileStorageRecord(
                    remoteUri = cursor.getString(cursor.getColumnIndex(StorageTable.REMOTE_URI)),
                    downloadId = cursor.getLong(cursor.getColumnIndex(StorageTable.DOWNLOAD_ID)),
                    localUri = cursor.getString(cursor.getColumnIndex(StorageTable.LOCAL_URI)),
                    status = StorageStatus.Companion.fromInt(
                            cursor.getInt(cursor.getColumnIndex(StorageTable.STATUS))),
                    percent = cursor.getInt(cursor.getColumnIndex(StorageTable.PERCENT))
            )
        }
    }

    class DeleteResolver : DefaultDeleteResolver<FileStorageRecord>() {
        public override fun mapToDeleteQuery(obj: FileStorageRecord): DeleteQuery {
            return DeleteQuery.builder()
                    .table(StorageTable.TABLE_NAME)
                    .where(StorageTable.REMOTE_URI + " = ?")
                    .whereArgs(obj.remoteUri)
                    .build()
        }
    }
}
