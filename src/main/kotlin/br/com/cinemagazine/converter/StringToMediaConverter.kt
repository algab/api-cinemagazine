package br.com.cinemagazine.converter

import br.com.cinemagazine.constants.ApiMessage.INVALID_MEDIA_TYPE
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.exception.BusinessException
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus.BAD_REQUEST

class StringToMediaConverter: Converter<String, Media> {
    override fun convert(source: String): Media {
        return Media.getMedia(source.uppercase()) ?: throw BusinessException(BAD_REQUEST, INVALID_MEDIA_TYPE)
    }
}