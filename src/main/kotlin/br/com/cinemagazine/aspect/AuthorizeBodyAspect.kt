package br.com.cinemagazine.aspect

import br.com.cinemagazine.annotation.authorization.AuthorizeBody
import br.com.cinemagazine.constants.ApiMessage.USER_UNAUTHORIZED
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.services.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component
import kotlin.reflect.cast

@Aspect
@Component
class AuthorizeBodyAspect(
    private val httpRequest: HttpServletRequest,
    private val tokenService: TokenService
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val fieldJTI = "jti"

    @Before("@annotation(br.com.cinemagazine.annotation.authorization.AuthorizeBody)")
    fun authorize(joinPoint: JoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val annotation = methodSignature.method.getAnnotation(AuthorizeBody::class.java)
        val token = httpRequest.getHeader(AUTHORIZATION).substring(7).trim()
        val body = annotation.clazz.cast(joinPoint.args[0])
        val jti = tokenService.getByField(token, fieldJTI)
        if (body.userId != jti) {
            logger.error("AuthorizeBodyAspect.authorize - {}", USER_UNAUTHORIZED.description)
            throw BusinessException(UNAUTHORIZED, USER_UNAUTHORIZED)
        }
    }
}