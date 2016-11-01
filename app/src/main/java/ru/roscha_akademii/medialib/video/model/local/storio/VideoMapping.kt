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
import ru.roscha_akademii.medialib.video.model.local.VideoTable

import ru.roscha_akademii.medialib.video.model.remote.Video

class VideoMapping : SQLiteTypeMapping<Video>(
        PutResolver(),
        GetResolver(),
        DeleteResolver()) {

    internal class PutResolver : DefaultPutResolver<Video>() {
        public override fun mapToInsertQuery(obj: Video): InsertQuery {
            return InsertQuery.builder()
                    .table(VideoTable.TABLE_NAME)
                    .build()
        }

        public override fun mapToUpdateQuery(obj: Video): UpdateQuery {
            return UpdateQuery.builder()
                    .table(VideoTable.TABLE_NAME)
                    .where(VideoTable.ID + " = ?")
                    .whereArgs(obj.id)
                    .build()
        }

        public override fun mapToContentValues(obj: Video): ContentValues {
            val contentValues = ContentValues()

            contentValues.put(VideoTable.VIDEO_URL, obj.videoUrl)
            contentValues.put(VideoTable.PICTURE_URL, obj.pictureUrl)
            contentValues.put(VideoTable.DESCRIPTION, obj.description)
            contentValues.put(VideoTable.ID, obj.id)
            contentValues.put(VideoTable.TITLE, obj.title)

            return contentValues
        }
    }

    internal class GetResolver : DefaultGetResolver<Video>() {
        override fun mapFromCursor(cursor: Cursor): Video {
            return Video(
                    cursor.getLong(cursor.getColumnIndex(VideoTable.ID)),
                    cursor.getString(cursor.getColumnIndex(VideoTable.TITLE)),
                    cursor.getString(cursor.getColumnIndex(VideoTable.PICTURE_URL)),
                    cursor.getString(cursor.getColumnIndex(VideoTable.DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(VideoTable.VIDEO_URL)))
        }
    }

    internal class DeleteResolver : DefaultDeleteResolver<Video>() {
        public override fun mapToDeleteQuery(obj: Video): DeleteQuery {
            return DeleteQuery.builder()
                    .table(VideoTable.TABLE_NAME)
                    .where(VideoTable.ID + " = ?")
                    .whereArgs(obj.id)
                    .build()
        }
    }
}