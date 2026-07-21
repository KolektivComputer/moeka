package dev.lizainslie.moeka.core.modules

/**
 * ModuleSource represents the origin of a module, indicating whether it is
 * built-in or added externally through jar loading.
 */
enum class PluginSource {
    /**
     * Internal modules are built into the bot and are typically part of its core
     * functionality.
     */
    INTERNAL,

    /**
     * External modules are added to the bot through jar loading, allowing for
     * additional features and functionalities to be integrated and hot loaded at
     * runtime.
     */
    EXTERNAL,
}
