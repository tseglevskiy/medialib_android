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
import org.joda.time.LocalDate
import ru.roscha_akademii.medialib.book.model.local.BookTable
import ru.roscha_akademii.medialib.book.model.remote.entity.Book

class BookMapping : SQLiteTypeMapping<Book>(
        PutResolver(),
        GetResolver(),
        DeleteResolver()) {

    internal class PutResolver : DefaultPutResolver<Book>() {
        public override fun mapToInsertQuery(obj: Book): InsertQuery {
            return InsertQuery.builder()
                    .table(BookTable.TABLE_NAME)
                    .build()
        }

        public override fun mapToUpdateQuery(obj: Book): UpdateQuery {
            return UpdateQuery.builder()
                    .table(BookTable.TABLE_NAME)
                    .where(BookTable.ID + " = ?")
                    .whereArgs(obj.id)
                    .build()
        }

        public override fun mapToContentValues(obj: Book): ContentValues {
            val contentValues = ContentValues()

            contentValues.put(BookTable.PICTURE_URL, obj.picture)
            contentValues.put(BookTable.DESCRIPTION, obj.description)
            contentValues.put(BookTable.ID, obj.id)
            contentValues.put(BookTable.TITLE, obj.title)

            return contentValues
        }
    }

    internal class GetResolver : DefaultGetResolver<Book>() {
        override fun mapFromCursor(cursor: Cursor): Book {
            return Book(
                    id = cursor.getLong(cursor.getColumnIndex(BookTable.ID)),
                    title = cursor.getString(cursor.getColumnIndex(BookTable.TITLE)),
                    description = cursor.getString(cursor.getColumnIndex(BookTable.DESCRIPTION)),
                    picture = cursor.getString(cursor.getColumnIndex(BookTable.PICTURE_URL))
            )

        }
    }

    internal class DeleteResolver : DefaultDeleteResolver<Book>() {
        public override fun mapToDeleteQuery(obj: Book): DeleteQuery {
            return DeleteQuery.builder()
                    .table(BookTable.TABLE_NAME)
                    .where(BookTable.ID + " = ?")
                    .whereArgs(obj.id)
                    .build()
        }
    }
}