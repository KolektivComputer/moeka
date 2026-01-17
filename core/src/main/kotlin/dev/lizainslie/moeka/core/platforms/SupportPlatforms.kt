package dev.lizainslie.moeka.core.platforms

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SupportPlatforms(
    vararg val platforms: KClass<out AnyPlatformAdapter>,
)
