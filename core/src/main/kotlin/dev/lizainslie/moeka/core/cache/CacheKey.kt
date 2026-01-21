package dev.lizainslie.moeka.core.cache

@JvmInline
value class CacheKey(
    val value: String
) {
    init {
        require(value.isNotBlank())
        require(value.matches(CACHE_KEY_REGEX))
    }

    val parts get() = value.split(".")

    companion object {
        private val CACHE_KEY_REGEX = Regex("""^(\w+\.)*\w+$""")
    }
}