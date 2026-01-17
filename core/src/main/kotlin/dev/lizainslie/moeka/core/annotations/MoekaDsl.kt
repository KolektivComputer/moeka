package dev.lizainslie.moeka.core.annotations

/** [DslMarker] for Moeka DSLs. */
@DslMarker
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class MoekaDsl
