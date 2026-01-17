package dev.lizainslie.moeka.util.color

val java.awt.Color.hexString: String get() = String.format("#%06X", 0xFFFFFF and rgb)
