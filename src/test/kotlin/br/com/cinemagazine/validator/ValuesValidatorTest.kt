package br.com.cinemagazine.validator

import br.com.cinemagazine.annotation.Values
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class ValuesValidatorTest: FunSpec({
    val validator = ValuesValidator()

    test("should return true when the value is present in the list") {
        validator.initialize(Values(values = arrayOf("A", "B")))

        val result = validator.isValid("a", null)

        result.shouldBeTrue()
    }

    test("should return false when the value isn't present in the list") {
        validator.initialize(Values(values = arrayOf("A", "B")))

        val result = validator.isValid("C", null)

        result.shouldBeFalse()
    }

    test("should return false when the value is null") {
        validator.initialize(Values(values = arrayOf("A", "B")))

        val result = validator.isValid(null, null)

        result.shouldBeFalse()
    }
})