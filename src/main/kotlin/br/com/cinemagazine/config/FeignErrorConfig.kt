package br.com.cinemagazine.config

import br.com.cinemagazine.exception.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import java.lang.Exception

@Configuration
class FeignErrorConfig: ErrorDecoder {
    override fun decode(value: String?, response: Response?): Exception {
        return FeignException(HttpStatus.valueOf(response!!.status()), response.reason()!!)
    }
}