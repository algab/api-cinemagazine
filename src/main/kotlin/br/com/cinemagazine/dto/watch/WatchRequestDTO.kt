package br.com.cinemagazine.dto.watch

import br.com.cinemagazine.annotation.Values
import br.com.cinemagazine.dto.user.UserIdDTO
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class WatchRequestDTO(
    @field:NotNull(message = "tmdbId field is required") val tmdbId: Long?,
    @field:Values(values = ["movie", "tv"], message = "media field accept values: movie and tv")
    val media: String?,
    @field:NotBlank(message = "userId field is required") override val userId: String?,
): UserIdDTO(userId)
