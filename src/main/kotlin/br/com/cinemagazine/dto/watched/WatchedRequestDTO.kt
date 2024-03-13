package br.com.cinemagazine.dto.watched

import br.com.cinemagazine.annotation.Values
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat

data class WatchedRequestDTO(
    @field:NotNull(message = "tmdbId field is required") val tmdbId: Long,
    @field:Values(values = ["Movie", "TV"], message = "media field accept values: Movie and TV")
    val media: String,
    @field:NotBlank(message = "userId field is required") val userId: String,
    @field:NotBlank(message = "date field is required")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val date: String
)