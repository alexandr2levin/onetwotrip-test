package com.onetwotrip.alexandr.presentation.utils

import com.google.gson.*
import org.slf4j.LoggerFactory
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

object LocalTimeGsonConverter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    private var dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun serialize(src: LocalTime, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(dateTimeFormatter.format(src))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime {
        return LocalTime.parse(json.asString, dateTimeFormatter)
    }
}