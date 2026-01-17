@file:OptIn(ExperimentalContracts::class)

package dev.lizainslie.moeka.core.placeholder

import dev.lizainslie.moeka.core.annotations.MoekaDsl
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@MoekaDsl
@Suppress("MatchingDeclarationName")
class PlaceholderMatrixDsl {
    val matrix = PlaceholderMatrix()

    fun replace(
        key: String,
        value: String,
    ) {
        matrix.addPlaceholder(key, value)
    }

    fun replace(
        key: String,
        getValue: () -> String,
    ) {
        matrix.addPlaceholder(key, getValue())
    }
}

fun placeholders(block: PlaceholderMatrixDsl.() -> Unit): PlaceholderMatrix {
    contract { callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE) }
    val dsl = PlaceholderMatrixDsl()
    dsl.block()
    return dsl.matrix
}
