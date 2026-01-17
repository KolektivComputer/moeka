package dev.lizainslie.moeka.core.validation

sealed interface ValidationResult {
    data object Valid : ValidationResult

    data class Invalid(
        val errors: List<String>,
    ) : ValidationResult
}
