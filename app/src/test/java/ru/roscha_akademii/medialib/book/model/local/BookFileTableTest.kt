package ru.roscha_akademii.medialib.book.model.local

import org.junit.Assert.assertTrue
import org.junit.Test

class BookFileTableTest {
    @Test
    fun createBookTable() {
        val query = BookFileTable.createTable()
        assertTrue(query.startsWith("create table ${BookFileTable.TABLE_NAME} "))
        assertTrue(query.contains("${BookFileTable.BOOK_ID} INTEGER"))
        assertTrue(query.contains("${BookFileTable.URL} STRING"))
    }

}