package ru.roscha_akademii.medialib.book.model.local.storio

import android.database.MatrixCursor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.roscha_akademii.medialib.BuildConfig
import ru.roscha_akademii.medialib.book.model.local.BookTable
import ru.roscha_akademii.medialib.book.model.remote.entity.Book
import ru.roscha_akademii.medialib.common.RobolectricMdiaLibApplication

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(21),
        manifest = "AndroidManifest.xml",
        application = RobolectricMdiaLibApplication::class,
        packageName = "ru.roscha_akademii.medialib")

class BookMappingTest {
    lateinit var mapping: BookMapping
    lateinit internal var putResolver: BookMapping.PutResolver
    lateinit internal var deleteResolver: BookMapping.DeleteResolver
    lateinit internal var getResolver: BookMapping.GetResolver

    val book1 = Book(
            id = 1111,
            title = "title one",
            picture = "picture url one",
            description = "description one")

    @Before
    fun setUp() {
        mapping = BookMapping()
        putResolver = mapping.putResolver() as BookMapping.PutResolver
        getResolver = mapping.getResolver() as BookMapping.GetResolver
        deleteResolver = mapping.deleteResolver() as BookMapping.DeleteResolver
    }

    @Test
    fun mapToUpdateQuery_identifyVideoById() {
        val query = putResolver.mapToUpdateQuery(book1)

        assertEquals(BookTable.TABLE_NAME, query.table())

        assertEquals("${BookTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(book1.id, query.whereArgs()[0].toLong())
    }

    @Test
    fun mapToContentValues() {
        val values = putResolver.mapToContentValues(book1)

        assertEquals(4, values.size())
        assertEquals(book1.id, values.get(BookTable.ID))
        assertEquals(book1.title, values.get(BookTable.TITLE))
        assertEquals(book1.description, values.get(BookTable.DESCRIPTION))
        assertEquals(book1.picture, values.get(BookTable.PICTURE_URL))
    }

    @Test
    fun mapToInsertQuery() {
        val query = putResolver.mapToInsertQuery(book1)

        assertEquals(BookTable.TABLE_NAME, query.table())
    }

    @Test
    fun mapFromCursor() {
        val cursor = MatrixCursor(arrayOf(
                BookTable.ID,
                BookTable.TITLE,
                BookTable.DESCRIPTION,
                BookTable.PICTURE_URL
        ))

        cursor.newRow()
                .add(book1.id)
                .add(book1.title)
                .add(book1.description)
                .add(book1.picture)

        cursor.moveToFirst()

        val video = getResolver.mapFromCursor(cursor)

        assertEquals(book1, video)
    }

    @Test
    fun mapToDeleteQuery() {
        val query = deleteResolver.mapToDeleteQuery(book1)

        assertEquals(BookTable.TABLE_NAME, query.table())

        assertEquals("${BookTable.ID}=?", query.where().replace(" ", "", true))

        assertEquals(1, query.whereArgs().size)
        assertEquals(book1.id, query.whereArgs()[0].toLong())
    }

}