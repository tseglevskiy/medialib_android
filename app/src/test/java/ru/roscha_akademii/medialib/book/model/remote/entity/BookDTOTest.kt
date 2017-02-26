package ru.roscha_akademii.medialib.book.model.remote.entity

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class BookDTOTest {

    @Test
    fun create() {
        val title = "title title"
        val descr = "descr credscr"
        val pic = "http://pic"
        val id = 123L
        val files = ArrayList<String>()
        files.add("one")
        files.add("two")

        val b = BookDTO(
                id = 123L,
                title = title,
                description = descr,
                picture = pic,
                files = files
        )

        assertEquals(id, b.id)
        assertEquals(descr, b.description)
        assertEquals(pic, b.picture)
        assertEquals(title, b.title)
        assertEquals(files.size, b.files!!.size)
        assertEquals(files[0], b.files!![0])
        assertEquals(files[1], b.files!![1])
    }

    @Test
    fun defaultValues() {
        val id = 456L
        val b = BookDTO(
                id = id
        )

        assertEquals(id, b.id)
        assertNull(b.files)
        assertNull(b.description)
        assertNull(b.picture)
        assertNull(b.title)
    }

    @Test
    fun nullValues() {
        val id = 456L
        val b = BookDTO(
                id = id,
                files = null,
                description = null,
                picture = null,
                title = null
        )

        assertEquals(id, b.id)
        assertNull(b.files)
        assertNull(b.description)
        assertNull(b.picture)
        assertNull(b.title)
    }
}