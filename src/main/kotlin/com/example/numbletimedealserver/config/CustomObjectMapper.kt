package com.example.numbletimedealserver.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import java.io.IOException
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class CustomObjectMapper : ObjectMapper() {
    init {
        val javaTimeModule = JavaTimeModule()
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        javaTimeModule.addSerializer(LocalTime::class.java, LocalTimeSerializer(timeFormatter))
        javaTimeModule.addDeserializer(LocalTime::class.java, CustomLocalTimeDeserializer(timeFormatter))

        this.registerModule(javaTimeModule)
        registerModule(KotlinModule.Builder().build())
    }
}

class CustomLocalTimeDeserializer(private val timeFormatter: DateTimeFormatter) :
    JsonDeserializer<LocalTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalTime {
        return LocalTime.parse(p?.getValueAsString(), timeFormatter)
    }
}