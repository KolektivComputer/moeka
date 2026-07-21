package dev.lizainslie.moeka.core.manual

import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import org.koin.core.component.KoinComponent

class ManualRegistryService : KoinComponent {
    val globalManuals: MutableList<Manual> = mutableListOf()
    val pluginManPages: MutableMap<String, MutableList<Manual>> = mutableMapOf()

    fun registerGlobalManPage(man: Manual) {
        globalManuals += man
    }

    fun registerPluginManPage(
        module: AbstractPlugin,
        man: Manual,
    ) {
        pluginManPages.getOrPut(module.name) { mutableListOf() } += man
    }

    fun getManPages() = globalManuals + pluginManPages.values.flatten()
}
