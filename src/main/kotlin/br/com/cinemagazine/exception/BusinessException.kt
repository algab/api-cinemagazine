package br.com.cinemagazine.exception

import br.com.cinemagazine.constants.ApiMessage
import org.springframework.http.HttpStatus

class BusinessException(val status: HttpStatus, message: ApiMessage) : RuntimeException(message.description)