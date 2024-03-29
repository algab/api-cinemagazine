package br.com.cinemagazine.annotation.authorization

import br.com.cinemagazine.dto.user.UserIdDTO
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class AuthorizeBody(
    val clazz: KClass<out UserIdDTO>
)
