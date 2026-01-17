package dev.lizainslie.moeka.platforms.discord.extensions

import dev.kord.core.entity.ReactionEmoji

fun ReactionEmoji.getIdentifier(): String =
    when (this) {
        is ReactionEmoji.Unicode -> name
        is ReactionEmoji.Custom -> id.value.toString()
    }
