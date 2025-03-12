package dev.mambo.lib.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.mambo.lib.local.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO : BaseDAO<CategoryEntity> {
    @Query("SELECT * FROM categories")
    fun getAllFlow(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Int): CategoryEntity?
}
