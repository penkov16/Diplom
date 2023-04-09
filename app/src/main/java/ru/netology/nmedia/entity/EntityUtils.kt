package ru.netology.nmedia.entity

@JvmName("toEntityLong")
internal fun List<Long>.fromDto(): String = joinToString(
    separator = LIST_DELIMITER.toString()
)

internal fun String.toDto(): List<Long> {
    if (this == "") {
        return emptyList()
    }
    return this.split(LIST_DELIMITER).map { it.toLong() }
}