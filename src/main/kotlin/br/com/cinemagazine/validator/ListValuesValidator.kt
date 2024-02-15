package br.com.cinemagazine.validator

import br.com.cinemagazine.annotation.ListValues
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ListValuesValidator: ConstraintValidator<ListValues, String> {

    private var values: List<String> = mutableListOf()

    override fun initialize(constraintAnnotation: ListValues?) {
        constraintAnnotation?.values?.forEach { values.addLast(it) }
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        for (data in values) {
            if (data.lowercase() == value?.lowercase()) {
                return true
            }
        }
        return false
    }
}