package br.com.cinemagazine.annotation.authorization

import br.com.cinemagazine.dto.user.UserIdDTO
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.reflect.KClass

@Target(FUNCTION, TYPE_PARAMETER)
@Retention(RUNTIME)
annotation class AuthorizeBody(
    val clazz: KClass<out UserIdDTO>
)
