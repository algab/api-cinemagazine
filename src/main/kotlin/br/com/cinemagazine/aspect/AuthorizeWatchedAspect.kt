package br.com.cinemagazine.aspect

import br.com.cinemagazine.constants.ApiMessage.USER_UNAUTHORIZED
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.WatchedRepository
import br.com.cinemagazine.services.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component

@Aspect
@Component
class AuthorizeWatchedAspect(
    private val httpRequest: HttpServletRequest,
    private val tokenService: TokenService,
    private val watchedRepository: WatchedRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val fieldJTI = "jti"

    @Before("@annotation(br.com.cinemagazine.annotation.authorization.AuthorizeWatch)")
    fun authorize(joinPoint: JoinPoint) {
        val id = joinPoint.args[0] as String
        val token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION).substring(7).trim()
        val jti = tokenService.getByField(token, fieldJTI)
        val watched = watchedRepository.findById(id)
        if (watched.isPresent && watched.get().userId != jti) {
            logger.error("AuthorizeWatchedAspect.authorize - {}", USER_UNAUTHORIZED.description)
            throw BusinessException(UNAUTHORIZED, USER_UNAUTHORIZED)
        }
    }
}