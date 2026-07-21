package dev.lizainslie.moeka.core.fs

import java.io.File
import java.nio.file.Paths

object BotFS {
    /**
     * The root bot directory
     */
    val baseDir: File =
        System.getProperty("moeka.bot.dir")?.let { File(it) }
            ?: Paths.get("").toAbsolutePath().toFile()

    /**
     * The directory where hosts are expected to place platform and module
     * configuration files
     */
    val configDir = baseDir.resolve("config")

    val platformConfigDir = configDir.resolve("platforms")
    val moduleConfigDir = configDir.resolve("plugins")

    /**
     * The directory where hosts are expected to place modules
     */
    val pluginsDir = baseDir.resolve("plugins")

    fun generateBaseStructure() {
        // create the config directory & its substructure if it doesn't exist
        if (!configDir.exists()) configDir.mkdirs()
        if (!platformConfigDir.exists()) platformConfigDir.mkdirs()
        if (!moduleConfigDir.exists()) moduleConfigDir.mkdirs()

        // create the modules directory if it doesn't exist
        if (!pluginsDir.exists()) pluginsDir.mkdirs()
    }

    object Temp {
        /**
         * The directory for temporary files
         */
        val dir: File =
            File("${System.getProperty("java.io.tmpdir")}/pitohui")
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
}
