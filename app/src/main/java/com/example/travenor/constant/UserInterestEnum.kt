package com.example.travenor.constant

enum class Place {
    MOUNTAIN,
    BEACH,
    CAVE
}

enum class Food {
    SEA_FOOD,
    ASIAN_FOOD,
    EUROPEAN_FOOD,
    FAST_FOOD
}

inline fun <reified T : Enum<T>> genericValueOf(value: String): T {
    return enumValueOf<T>(value)
}

inline fun <reified T : Enum<T>> isNameOfEnum(value: String): Boolean {
    return enumValues<T>().any { it.name == value }
}
