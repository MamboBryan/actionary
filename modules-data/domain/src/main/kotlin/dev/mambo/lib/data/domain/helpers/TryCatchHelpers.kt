package dev.mambo.lib.data.domain.helpers

import timber.log.Timber

fun <T> tryOrNull(block: () -> T) =
    try {
        block()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
