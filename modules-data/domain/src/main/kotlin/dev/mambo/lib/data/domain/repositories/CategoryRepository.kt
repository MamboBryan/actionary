package dev.mambo.lib.data.domain.repositories

import dev.mambo.lib.data.domain.helpers.DataResult
import dev.mambo.lib.data.domain.models.CategoryDomain
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun create(name: String): DataResult<CategoryDomain>

    suspend fun delete(category: CategoryDomain): DataResult<Boolean>

    fun getAllFlow(): Flow<List<CategoryDomain>>
}
