package dev.lizainslie.moeka.core.validation.validators.composite

import dev.lizainslie.moeka.core.validation.ValidationResult
import dev.lizainslie.moeka.core.validation.collapseResultsAll
import dev.lizainslie.moeka.core.validation.collapseResultsAny
import dev.lizainslie.moeka.core.validation.collapseResultsOne

enum class CompositeValidationMode(
    val collapse: (List<ValidationResult>) -> ValidationResult,
) {
    ALL(::collapseResultsAll),
    ANY(::collapseResultsAny),
    ONE(::collapseResultsOne),
}
