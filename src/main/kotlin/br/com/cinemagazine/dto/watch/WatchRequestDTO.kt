package br.com.cinemagazine.dto.watch

import br.com.cinemagazine.annotation.Values
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class WatchRequestDTO(
    @field:NotNull(message = "tmdbId field is required") val tmdbId: Long,
    @field:Values(values = ["Movie", "TV"], message = "media field accept values: Movie and TV")
    val media: String,
    @field:NotBlank(message = "userId field is required") val userId: String,
)
