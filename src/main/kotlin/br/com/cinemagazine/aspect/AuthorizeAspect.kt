package br.com.cinemagazine.aspect

import br.com.cinemagazine.constants.ApiMessage.TOKEN_NOT_FOUND
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.services.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
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
    @Before("@annotation(br.com.cinemagazine.annotation.authorization.Authorize) || " +
            "@annotation(br.com.cinemagazine.annotation.authorization.AuthorizeBody) || " +
            "@annotation(br.com.cinemagazine.annotation.authorization.AuthorizeUser)"
    )
    fun authorize() {
        val token = request.getHeader(AUTHORIZATION)
        if (Objects.isNull(token)) {
            throw BusinessException(UNAUTHORIZED, TOKEN_NOT_FOUND)
        }
        tokenService.validateAccessToken(token.substring(7).trim())
    }
}