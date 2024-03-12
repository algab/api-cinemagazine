package br.com.cinemagazine.dto.production

import br.com.cinemagazine.serializer.ImageSerializer
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonInclude(NON_NULL)
data class CastTvDTO(
    val name: String,
    val roles: List<RoleTvDTO>,
    @JsonSerialize(using = ImageSerializer::class)
    val image: String?
)
