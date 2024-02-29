package br.com.cinemagazine.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import io.kotest.core.spec.style.FunSpec
import io.mockk.spyk
import io.mockk.verify

class ImageSerializerTest: FunSpec({

    val jsonGenerator = spyk<JsonGenerator>()
    val serializerProvider = spyk<SerializerProvider>()
    val serializer = ImageSerializer("https://image.tmdb.org/t/p/original")

    test("should serialize with successful") {
        serializer.serialize("/test.png", jsonGenerator, serializerProvider)

        verify(exactly = 1) { jsonGenerator.writeString(any(String::class)) }
    }
})