package br.com.cinemagazine.annotation

import br.com.cinemagazine.validator.ValuesValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValuesValidator::class])
annotation class Values(
    val message: String = "",
    val values: Array<String> = [],
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
