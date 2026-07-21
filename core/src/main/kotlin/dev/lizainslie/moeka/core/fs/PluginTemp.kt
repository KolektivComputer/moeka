package dev.lizainslie.moeka.core.fs

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class PluginTemp(
    pluginName: String,
) : KoinComponent {
    val contexts = mutableListOf<PluginTempContext>()

    val tempFs by inject<TempFs>()

    val directory: File = tempFs.plugins.resolve(pluginName)

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
