package dev.lizainslie.moeka.core.config

interface ConfigBase {
    fun validate(): Boolean

    fun onLoad() {}
}
