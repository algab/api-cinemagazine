package br.com.cinemagazine.dto.production

import br.com.cinemagazine.serializer.ImageSerializer
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonInclude(NON_NULL)
data class CrewMovieDTO(
    val name: String,
    val job: String,
    @JsonSerialize(using = ImageSerializer::class)
    val image: String?
)
