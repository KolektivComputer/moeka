package dev.lizainslie.moeka.util.std

import java.util.EnumSet

/**
 * Shorthand for [EnumSet].allOf([T]::class.java)
 */
inline fun <reified T : Enum<T>> enumSetAll(): EnumSet<T> = EnumSet.allOf(T::class.java)
