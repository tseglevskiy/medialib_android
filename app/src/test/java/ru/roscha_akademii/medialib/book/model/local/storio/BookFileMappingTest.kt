package ru.roscha_akademii.medialib.book.model.local.storio

import android.database.MatrixCursor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.book.model.local.BookFileTable
import ru.roscha_akademii.medialib.book.model.local.entity.BookFile
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class BookFileMappingTest {
    lateinit var mapping: BookFileMapping
    lateinit internal var putResolver: BookFileMapping.PutResolver
    lateinit internal var deleteResolver: BookFileMapping.DeleteResolver
    lateinit internal var getResolver: BookFileMapping.GetResolver

    val book1 = BookFile(
            id = 1111,
            bookId = 5678,
            url = "url one")

    @Before
    fun setUp() {
        mapping = BookFileMapping()
        putResolver = mapping.putResolver() as BookFileMapping.PutResolver
        getResolver = mapping.getResolver() as BookFileMapping.GetResolver
        deleteResolver = mapping.deleteResolver() as BookFileMapping.DeleteResolver
    }

    @Test
    fun mapToUpdateQuery_identifyVideoById() {
        val query = putResolver.mapToUpdateQuery(book1)

        assertEquals(BookFileTable.TABLE_NAME, query.table())

        assertEquals("${BookFileTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(book1.id, query.whereArgs()[0].toLong())
    }

    @Test
    fun mapToContentValues() {
        val values = putResolver.mapToContentValues(book1)

        assertEquals(3, values.size())
        assertEquals(book1.id, values.get(BookFileTable.ID))
        assertEquals(book1.bookId, values.get(BookFileTable.BOOK))
        assertEquals(book1.url, values.get(BookFileTable.URL))
    }

    @Test
    fun mapToInsertQuery() {
        val query = putResolver.mapToInsertQuery(book1)

        assertEquals(BookFileTable.TABLE_NAME, query.table())
    }

    @Test
    fun mapFromCursor() {
        val cursor = MatrixCursor(arrayOf(
                BookFileTable.ID,
                BookFileTable.BOOK,
                BookFileTable.URL
        ))

        cursor.newRow()
                .add(book1.id)
                .add(book1.bookId)
                .add(book1.url)

        cursor.moveToFirst()

        val video = getResolver.mapFromCursor(cursor)

        assertEquals(book1, video)
    }

    @Test
    fun mapToDeleteQuery() {
        val query = deleteResolver.mapToDeleteQuery(book1)

        assertEquals(BookFileTable.TABLE_NAME, query.table())

        assertEquals("${BookFileTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(book1.id, query.whereArgs()[0].toLong())
    }

}