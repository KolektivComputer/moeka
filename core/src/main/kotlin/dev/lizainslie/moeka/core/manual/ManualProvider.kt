package dev.lizainslie.moeka.core.manual

import org.koin.core.component.KoinComponent

interface ManualProvider : KoinComponent {
    fun registerManPage(man: Manual)

    fun registerManPage(
        identifier: String,
        title: String,
        builder: ManualDsl.() -> Unit,
    ) {
        val manual = ManualDsl(identifier, title).apply(builder).build()
        registerManPage(manual)
    }
}
