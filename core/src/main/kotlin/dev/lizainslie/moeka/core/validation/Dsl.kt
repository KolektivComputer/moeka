@file:OptIn(ExperimentalContracts::class)

package dev.lizainslie.moeka.core.validation

import dev.lizainslie.moeka.core.annotations.MoekaDsl
import dev.lizainslie.moeka.core.validation.validators.CoercingValidator
import dev.lizainslie.moeka.core.validation.validators.StringValidators
import dev.lizainslie.moeka.core.validation.validators.composite.CompositeValidationMode
import dev.lizainslie.moeka.core.validation.validators.composite.CompositeValidator
import dev.lizainslie.moeka.core.validation.validators.generic.InCollectionValidator
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private interface BaseValidationDsl<T : Any> {
    val validator: Validator<T>
}

@MoekaDsl
class ValidationDsl<T : Any> : BaseValidationDsl<T> {
    private lateinit var _validator: Validator<T>

    val validatorSet get() = ::_validator.isInitialized

    @Throws(IllegalStateException::class)
    fun setValidator(validator: Validator<T>) {
        if (validatorSet) {
            throw IllegalStateException("Validator already defined")
        }
        _validator = validator
    }

    @Throws(IllegalStateException::class)
    fun composite(
        mode: CompositeValidationMode = CompositeValidationMode.ALL,
        compositeBuilder: CompositeValidatorDsl<T>.() -> Unit,
    ) {
        contract { callsInPlace(compositeBuilder, InvocationKind.EXACTLY_ONCE) }

        val dsl = CompositeValidatorDsl<T>(mode).apply(compositeBuilder)
        setValidator(dsl.validator)
    }

    @Throws(IllegalStateException::class)
    inline fun <reified TNew : Any> isType(validationBuilder: ValidationDsl<TNew>.() -> Unit) {
        contract { callsInPlace(validationBuilder, InvocationKind.EXACTLY_ONCE) }

        val dsl = ValidationDsl<TNew>().apply(validationBuilder)
        setValidator(CoercingValidator.create<T, TNew>(dsl.validator))
    }

    override val validator: Validator<T> get() {
        if (!validatorSet) {
            throw IllegalStateException("You must set a validator!")
        }

        return _validator
    }
}

@MoekaDsl
class CompositeValidatorDsl<T : Any>(
    private val mode: CompositeValidationMode = CompositeValidationMode.ALL,
) : BaseValidationDsl<T> {
    val validators = mutableListOf<Validator<T>>()

    fun addValidator(validator: Validator<T>) {
        validators += validator
    }

    operator fun plus(validator: Validator<T>) {
        addValidator(validator)
    }

    fun validator(validatorBuilder: ValidationDsl<T>.() -> Unit) {
        contract { callsInPlace(validatorBuilder, InvocationKind.EXACTLY_ONCE) }
        addValidator(buildValidator(validatorBuilder))
    }

    override val validator get() = CompositeValidator(mode, validators)
}

fun <T : Any> buildValidator(validatorBuilder: ValidationDsl<T>.() -> Unit): Validator<T> {
    contract { callsInPlace(validatorBuilder, InvocationKind.EXACTLY_ONCE) }

    val dsl = ValidationDsl<T>().apply(validatorBuilder)
    return dsl.validator
}

@Throws(IllegalStateException::class)
fun ValidationDsl<String>.hexColor(allowAlpha: Boolean = false) {
    setValidator(StringValidators.HexColor(allowAlpha))
}

@Throws(IllegalStateException::class)
fun <T : Any> ValidationDsl<T>.isIn(collection: Collection<T>) {
    setValidator(InCollectionValidator(collection))
}
