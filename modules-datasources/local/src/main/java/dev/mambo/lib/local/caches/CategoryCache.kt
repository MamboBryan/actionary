package dev.mambo.lib.local.caches

import dev.mambo.lib.local.entities.CategoryEntity

data class CategoryCache(
    val id: Int,
    val name: String,
)

internal fun CategoryCache.toCategoryEntity() = CategoryEntity(name = name, id = id)

internal fun CategoryEntity.toCategoryCache() = CategoryCache(id = id, name = name)
