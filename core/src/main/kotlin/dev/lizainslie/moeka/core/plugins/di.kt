package dev.lizainslie.moeka.core.plugins

import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import org.koin.core.qualifier.TypeQualifier

val PluginScopeArchetype = TypeQualifier(AbstractPlugin::class)