package ru.roscha_akademii.medialib.book.model.local.storio

import android.content.ContentValues
import android.database.Cursor
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio.sqlite.queries.InsertQuery
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery
import ru.roscha_akademii.medialib.book.model.local.BookFileTable
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile

class BookFileMapping : SQLiteTypeMapping<BookFile>(
        PutResolver(),
        GetResolver(),
        DeleteResolver()) {

    internal class PutResolver : DefaultPutResolver<BookFile>() {
        public override fun mapToInsertQuery(obj: BookFile): InsertQuery {
            return InsertQuery.builder()
                    .table(BookFileTable.TABLE_NAME)
                    .build()
        }

        public override fun mapToUpdateQuery(obj: BookFile): UpdateQuery {
            return UpdateQuery.builder()
                    .table(BookFileTable.TABLE_NAME)
                    .where(BookFileTable.ID + " = ?")
                    .whereArgs(obj.id)
                    .build()
        }

        public override fun mapToContentValues(obj: BookFile): ContentValues {
            val contentValues = ContentValues()

            contentValues.put(BookFileTable.URL, obj.url)
            contentValues.put(BookFileTable.ID, obj.id)
            contentValues.put(BookFileTable.BOOK, obj.bookId)

            return contentValues
        }
    }

    internal class GetResolver : DefaultGetResolver<BookFile>() {
        override fun mapFromCursor(cursor: Cursor): BookFile {
            return BookFile(
                    id = cursor.getLong(cursor.getColumnIndex(BookFileTable.ID)),
                    bookId = cursor.getLong(cursor.getColumnIndex(BookFileTable.BOOK)),
                    url = cursor.getString(cursor.getColumnIndex(BookFileTable.URL))
            )
        }
    }

    internal class DeleteResolver : DefaultDeleteResolver<BookFile>() {
        public override fun mapToDeleteQuery(obj: BookFile): DeleteQuery {
            return DeleteQuery.builder()
                    .table(BookFileTable.TABLE_NAME)
                    .where(BookFileTable.ID + " = ?")
                    .whereArgs(obj.id)
                    .build()
        }
    }
}