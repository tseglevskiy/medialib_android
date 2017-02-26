package ru.roscha_akademii.medialib.book.model.local.entity

import org.junit.Assert.*
import org.junit.Test
import ru.roscha_akademii.medialib.book.model.remote.entity.BookDTO
import java.util.*

class BookTest {

    @Test
    fun create() {
        val title = "title title"
        val descr = "descr credscr"
        val pic = "http://pic"
        val id = 123L
        val files = ArrayList<String>()
        files.add("one")
        files.add("two")

        val b = Book(
                id = 123L,
                title = title,
                description = descr,
                picture = pic
        )

        assertEquals(id, b.id)
        assertEquals(descr, b.description)
        assertEquals(pic, b.picture)
        assertEquals(title, b.title)
    }

    @Test
    fun defaultValues() {
        val id = 456L
        val b = Book(
                id = id
        )

        assertEquals(id, b.id)
        assertNull(b.description)
        assertNull(b.picture)
        assertNull(b.title)
    }

    @Test
    fun nullValues() {
        val id = 456L
        val b = Book(
                id = id,
                description = null,
                picture = null,
                title = null
        )

        assertEquals(id, b.id)
        assertNull(b.description)
        assertNull(b.picture)
        assertNull(b.title)
    }

    @Test
    fun createFromDto() {
        val d = BookDTO(
                id = 234L,
                description = "desc desc",
                picture = "http://picpic",
                title = "tititi"
        )

        val b = Book(d)

        assertEquals(d.id, b.id)
        assertEquals(d.title, b.title)
        assertEquals(d.description, b.description)
        assertEquals(d.picture, b.picture)
    }
}