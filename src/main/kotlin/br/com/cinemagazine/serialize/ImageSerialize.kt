package br.com.cinemagazine.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value

class ImageSerialize(@Value("\${tmdb.image}") private val urlImage: String): JsonSerializer<String>() {
    override fun serialize(field: String?, jsonGenerator: JsonGenerator?, serializerProvider: SerializerProvider?) {
        jsonGenerator?.writeString("${this.urlImage}/${field}")
    }
}