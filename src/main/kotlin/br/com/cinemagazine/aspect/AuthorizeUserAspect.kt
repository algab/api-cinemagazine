package br.com.cinemagazine.aspect

import br.com.cinemagazine.constants.ApiMessage.USER_UNAUTHORIZED
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.services.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component

@Aspect
@Component
class AuthorizeUserAspect(
    private val httpRequest: HttpServletRequest,
    private val tokenService: TokenService
) {

    val fieldJTI = "jti"

    @Before("@annotation(br.com.cinemagazine.annotation.authorization.AuthorizeUser)")
    fun authorize(joinPoint: JoinPoint) {
        val token = httpRequest.getHeader(AUTHORIZATION).substring(7).trim()
        val userId = joinPoint.args[0] as String
        val jti = tokenService.getByField(token, fieldJTI)
        if (userId != jti) {
            throw BusinessException(UNAUTHORIZED, USER_UNAUTHORIZED)
        }
    }
}