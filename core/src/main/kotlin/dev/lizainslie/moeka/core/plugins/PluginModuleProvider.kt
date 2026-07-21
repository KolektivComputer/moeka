package dev.lizainslie.moeka.core.plugins

import org.koin.core.module.Module

interface PluginModuleProvider {
    fun createModule(): Module
}