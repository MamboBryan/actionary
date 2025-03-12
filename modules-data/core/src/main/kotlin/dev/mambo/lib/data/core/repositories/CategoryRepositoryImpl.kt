package dev.mambo.lib.data.core.repositories

import dev.mambo.lib.data.core.mappers.asDataResult
import dev.mambo.lib.data.core.mappers.toCategoryCache
import dev.mambo.lib.data.core.mappers.toCategoryDomain
import dev.mambo.lib.data.domain.helpers.DataResult
import dev.mambo.lib.data.domain.models.CategoryDomain
import dev.mambo.lib.data.domain.repositories.CategoryRepository
import dev.mambo.lib.local.sources.CategoryLocalSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryImpl(
    private val localSource: CategoryLocalSource,
) : CategoryRepository {
    override suspend fun create(name: String): DataResult<CategoryDomain> {
        return localSource.create(name = name).asDataResult { it.toCategoryDomain() }
    }

    override suspend fun delete(category: CategoryDomain): DataResult<Boolean> {
        return localSource.delete(category = category.toCategoryCache()).asDataResult { it }
    }

    override fun getAllFlow(): Flow<List<CategoryDomain>> {
        return localSource.getAllFlow().mapLatest { list -> list.map { it.toCategoryDomain() } }
    }
}
