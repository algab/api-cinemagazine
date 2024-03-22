package br.com.cinemagazine.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class FeignException(val status: HttpStatus, override val message: String): RuntimeException(message)
