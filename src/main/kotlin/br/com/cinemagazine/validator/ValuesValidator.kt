package br.com.cinemagazine.validator

import br.com.cinemagazine.annotation.Values
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValuesValidator: ConstraintValidator<Values, String> {

    private var values: List<String> = mutableListOf()

    override fun initialize(annotation: Values) {
        annotation.values.forEach { values.addLast(it) }
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