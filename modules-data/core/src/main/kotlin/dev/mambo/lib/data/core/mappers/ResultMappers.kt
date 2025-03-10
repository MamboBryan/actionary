package dev.mambo.lib.data.core.mappers

import dev.mambo.lib.data.domain.helpers.DataResult
import dev.mambo.lib.local.helpers.LocalResult

fun <T, R> LocalResult<T>.asDataResult(block: (T) -> R): DataResult<R> =
    when (this) {
        is LocalResult.Error -> DataResult.Error(message = message)
        is LocalResult.Success -> DataResult.Success(data = block(data))
    }
