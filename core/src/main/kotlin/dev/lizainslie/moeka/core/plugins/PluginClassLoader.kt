package dev.lizainslie.moeka.core.plugins

import java.net.URL
import java.net.URLClassLoader

class PluginClassLoader(
    jarUrl: URL,
    parent: ClassLoader,
) : URLClassLoader(arrayOf(jarUrl), parent) {
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
