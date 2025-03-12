package dev.mambo.lib.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.mambo.lib.local.entities.TaskEntity
import dev.mambo.lib.local.entities.TaskWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO : BaseDAO<TaskEntity> {
    @Transaction
    @Query("SELECT * FROM tasks")
    fun fetchAllWithCategory(): Flow<List<TaskWithCategory>>

    @Transaction
    @Query(
        """
        SELECT * FROM tasks
        WHERE tasks.priority = :priority
        """,
    )
    fun fetchAllWithCategoryByPriority(priority: String): Flow<List<TaskWithCategory>>

    @Transaction
    @Query(
        """
        SELECT * FROM tasks
        WHERE tasks.category_id = :categoryId
        """,
    )
    fun fetchAllWithCategoryByCategory(categoryId: Int): Flow<List<TaskWithCategory>>

    @Transaction
    @Query(
        """
        SELECT * FROM tasks
        WHERE tasks.category_id = :categoryId AND tasks.priority = :priority
        """,
    )
    fun fetchAllWithCategoryByPriorityAndCategory(
        priority: String,
        categoryId: Int,
    ): Flow<List<TaskWithCategory>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id ")
    fun fetchById(id: Int): TaskWithCategory?

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id ")
    fun fetchByIdFlow(id: Int): Flow<TaskWithCategory?>
}
