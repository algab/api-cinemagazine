package br.com.cinemagazine.aspect

import br.com.cinemagazine.constants.ApiMessage.TOKEN_NOT_FOUND
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.services.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component
import java.util.Objects

@Aspect
@Component
class AuthorizeAspect(
    private val request: HttpServletRequest,
    private val tokenService: TokenService
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Before("@annotation(br.com.cinemagazine.annotation.authorization.Authorize)")
    fun authorize() {
        val token = request.getHeader(AUTHORIZATION)
        if (Objects.isNull(token)) {
            logger.error("AuthorizeAspect.authorize - {}", TOKEN_NOT_FOUND.description)
            throw BusinessException(UNAUTHORIZED, TOKEN_NOT_FOUND)
        }
        tokenService.validateAccessToken(token.substring(7).trim())
    }
}