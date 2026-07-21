package dev.lizainslie.moeka.core.plugins.ext.koin

import dev.lizainslie.moeka.core.annotations.MoekaDsl
import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module._factoryInstanceFactory
import org.koin.core.module._scopedInstanceFactory
import org.koin.core.qualifier.Qualifier
import org.koin.plugin.module.dsl.factory
import kotlin.reflect.KClass

@OptIn(KoinInternalApi::class)
@MoekaDsl
class PluginScopeDsl<TPlugin : AbstractPlugin>(
    val scopeQualifier: Qualifier,
    val module: Module,
    val pluginClass: KClass<out TPlugin>
) {
    inline fun <reified T : AbstractPlugin> plugin(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>
    ) = scoped<AbstractPlugin>(qualifier, definition)

    inline fun <reified T> scoped(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
    ): KoinDefinition<T> {
        val def = _scopedInstanceFactory(qualifier, definition, scopeQualifier)
        module.indexPrimaryType(def)
        return KoinDefinition(module, def)
    }

    inline fun <reified T> factory(
        qualifier: Qualifier? = null,
        noinline definition: Definition<T>,
    ): KoinDefinition<T> {
        val factory = _factoryInstanceFactory(qualifier, definition, scopeQualifier)
        module.indexPrimaryType(factory)
        return KoinDefinition(module, factory)
    }
}
