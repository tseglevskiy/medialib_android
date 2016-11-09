package ru.roscha_akademii.medialib.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalDateDeserializerTest {
    lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        .create()
    }

    @Test
    fun deserialize() {
        val date = "2001-01-01"

        val result = gson.fromJson(date, LocalDate::class.java)

        assertEquals(LocalDate.parse(date), result)
        assertEquals(date, result.toString())
    }

}