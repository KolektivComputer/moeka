package dev.lizainslie.moeka.core.fs

import java.io.File

class TempFs {
    /**
     * The directory for temporary files
     */
    val dir: File =
        File("${System.getProperty("java.io.tmpdir")}/moeka")
            .absoluteFile
            .apply { if (!exists()) mkdirs() }

    val plugins =
        dir
            .resolve("plugins")
            .apply { if (!exists()) mkdirs() }

    /**
     * Cleans up all files in the temp directory
     */
    fun cleanup() {
        dir.listFiles()?.forEach { it.deleteRecursively() }
    }
}