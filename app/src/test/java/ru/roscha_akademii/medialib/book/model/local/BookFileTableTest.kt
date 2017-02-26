package ru.roscha_akademii.medialib.book.model.local

import org.junit.Assert.*
import org.junit.Test

class BookFileTableTest {
    @Test
    fun createBookTable() {
        val query = BookFileTable.createTable()
        assertTrue(query.startsWith("create table ${BookFileTable.TABLE_NAME} "))
        assertTrue(query.contains("${BookFileTable.ID} INTEGER PRIMARY KEY"))
        assertTrue(query.contains("${BookFileTable.BOOK} INTEGER"))
        assertTrue(query.contains("${BookFileTable.URL} STRING"))
    }

}