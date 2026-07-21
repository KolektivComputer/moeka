package dev.lizainslie.moeka.core.plugins.example

import dev.lizainslie.moeka.core.plugins.ext.pluginScope
import org.koin.dsl.module

val examplePluginModule = module {
    pluginScope<ExamplePlugin> {
        plugin<ExamplePlugin> { params ->
            ExamplePlugin(params.get())
        }
    }
}