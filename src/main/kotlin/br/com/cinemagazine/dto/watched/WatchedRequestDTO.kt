package br.com.cinemagazine.dto.watched

import br.com.cinemagazine.annotation.Values
import br.com.cinemagazine.dto.user.UserIdDTO
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat

data class WatchedRequestDTO(
    @field:NotNull(message = "tmdbId field is required") val tmdbId: Long?,
    @field:Values(values = ["movie", "tv"], message = "media field accept values: movie and tv")
    val media: String?,
    @field:NotBlank(message = "userId field is required") override val userId: String?,
    @field:NotBlank(message = "date field is required")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val date: String?
): UserIdDTO(userId)