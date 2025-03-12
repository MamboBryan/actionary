package dev.mambo.lib.data.core.mappers

import dev.mambo.lib.data.domain.models.CategoryDomain
import dev.mambo.lib.local.caches.CategoryCache

fun CategoryCache.toCategoryDomain() = CategoryDomain(id = id, name = name)

fun CategoryDomain.toCategoryCache() = CategoryCache(id = id, name = name)
