package ru.roscha_akademii.medialib.net

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.joda.time.LocalDate
import java.lang.reflect.Type

class LocalDateDeserializer : JsonDeserializer<LocalDate> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate? {
        val src = json?.asString ?: return null
        return LocalDate.parse(src)
    }

}