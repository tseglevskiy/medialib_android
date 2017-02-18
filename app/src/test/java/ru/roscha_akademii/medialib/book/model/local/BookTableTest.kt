package ru.roscha_akademii.medialib.book.model.local

import org.junit.Assert.*
import org.junit.Test

class BookTableTest {
    @Test
    fun createBookTable() {
        val query = BookTable.createTable()
        assertTrue(query.startsWith("create table ${BookTable.TABLE_NAME} "))
        assertTrue(query.contains("${BookTable.ID} INTEGER PRIMARY KEY"))
        assertTrue(query.contains("${BookTable.TITLE} STRING"))
        assertTrue(query.contains("${BookTable.DESCRIPTION} STRING"))
        assertTrue(query.contains("${BookTable.PICTURE_URL} STRING"))
    }

}