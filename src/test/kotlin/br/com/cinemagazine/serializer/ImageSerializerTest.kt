package br.com.cinemagazine.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.verify

class ImageSerializerTest: FunSpec({

    val hostImage = "https://image.tmdb.org/t/p/original"

    val jsonGenerator = spyk<JsonGenerator>()
    val serializerProvider = spyk<SerializerProvider>()
    val serializer = ImageSerializer(hostImage)

    afterTest { clearAllMocks() }

    test("should serialize with successful") {
        serializer.serialize("/test.png", jsonGenerator, serializerProvider)

        verify(exactly = 1) { jsonGenerator.writeString("${hostImage}/test.png") }
    }

    test("should not serialize when the field is null") {
        serializer.serialize(null, jsonGenerator, serializerProvider)

        verify(exactly = 1) { jsonGenerator.writeString("") }
    }

    test("should not serialize when the field is empty") {
        serializer.serialize("", jsonGenerator, serializerProvider)

        verify(exactly = 1) { jsonGenerator.writeString("") }
    }
})