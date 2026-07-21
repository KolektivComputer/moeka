package dev.lizainslie.moeka.core.manual

import dev.lizainslie.moeka.core.plugins.AbstractPlugin

class ManualRegistry {
    val globalManuals: MutableList<Manual> = mutableListOf()
    val moduleManPages: MutableMap<String, MutableList<Manual>> = mutableMapOf()

    fun registerGlobalManPage(man: Manual) {
        globalManuals += man
    }

    fun registerModuleManPage(
        module: AbstractPlugin,
        man: Manual,
    ) {
        moduleManPages.getOrPut(module.name) { mutableListOf() } += man
    }

    fun getManPages() = globalManuals + moduleManPages.values.flatten()
}
