package dev.lizainslie.moeka.core.fs

import java.io.File

class PluginTemp(
    val directory: File,
) {
    val contexts = mutableListOf<PluginTempContext>()

    init {
        if (directory.exists() && !directory.isDirectory) {
            throw IllegalArgumentException("Module temp context path ${directory.path} exists and is not a directory")
        }

        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    fun cleanup() {
        contexts.forEach { it.cleanup() }
        contexts.clear()
        directory.deleteRecursively()
    }

    fun createContext(): PluginTempContext {
        val context = PluginTempContext(this)
        contexts += context
        return context
    }

    fun removeContext(context: PluginTempContext) {
        context.cleanup()
        contexts -= context
    }
}
