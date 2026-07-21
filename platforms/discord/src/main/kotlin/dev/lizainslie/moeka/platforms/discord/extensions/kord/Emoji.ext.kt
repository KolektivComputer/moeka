package dev.lizainslie.moeka.platforms.discord.extensions.kord

import dev.kord.core.entity.ReactionEmoji

fun ReactionEmoji.getIdentifier(): String =
    when (this) {
        is ReactionEmoji.Unicode -> name
        is ReactionEmoji.Custom -> id.value.toString()
    }
