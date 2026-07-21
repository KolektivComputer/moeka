package dev.lizainslie.moeka.core.plugins

import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import org.koin.core.component.KoinComponent

interface BasePluginsProvider : KoinComponent {
    fun getPlugins(): List<AbstractPlugin>
}