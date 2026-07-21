package dev.lizainslie.moeka.core.plugins.registry

import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import java.net.URLClassLoader
import kotlin.reflect.KClass

class PluginClassLoader(
    jarUrl: URL,
    parent: ClassLoader,
) : URLClassLoader(arrayOf(jarUrl), parent) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Suppress("UNCHECKED_CAST")
    fun loadPluginClass(name: String): Result<Class<out AbstractPlugin>> {
        val klass = loadClass(name) as? Class<out AbstractPlugin> ?: run {
            log.warn("")
            return Result.failure(ClassNotFoundException("Plugin class not found: $name"))
        }

        return Result.success(klass)
    }

    override fun loadClass(
        name: String,
        resolve: Boolean,
    ): Class<*> =
        try {
            findClass(name)
        } catch (_: ClassNotFoundException) {
            super.loadClass(name, resolve)
        }
}