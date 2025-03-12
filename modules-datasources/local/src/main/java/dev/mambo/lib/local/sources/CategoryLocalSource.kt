package dev.mambo.lib.local.sources

import dev.mambo.lib.local.caches.CategoryCache
import dev.mambo.lib.local.caches.toCategoryCache
import dev.mambo.lib.local.caches.toCategoryEntity
import dev.mambo.lib.local.dao.CategoryDAO
import dev.mambo.lib.local.entities.CategoryEntity
import dev.mambo.lib.local.helpers.LocalResult
import dev.mambo.lib.local.helpers.safeTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CategoryLocalSource {
    suspend fun create(name: String): LocalResult<CategoryCache>

    suspend fun delete(category: CategoryCache): LocalResult<Boolean>

    fun getAllFlow(): Flow<List<CategoryCache>>
}

internal class CategoryLocalSourceImpl(
    private val dispatcher: CoroutineDispatcher,
    private val dao: CategoryDAO,
) : CategoryLocalSource {
    override suspend fun create(name: String): LocalResult<CategoryCache> =
        safeTransaction(dispatcher) {
            val id = dao.insert(CategoryEntity(name = name))
            val entity = dao.getById(id = id.toInt()) ?: throw Exception("Category not found")
            entity.toCategoryCache()
        }

    override suspend fun delete(category: CategoryCache): LocalResult<Boolean> =
        safeTransaction(dispatcher) {
            dao.delete(category.toCategoryEntity())
            true
        }

    override fun getAllFlow(): Flow<List<CategoryCache>> = dao.getAllFlow().map { list -> list.map { it.toCategoryCache() } }
}
