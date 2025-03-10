package dev.mambo.lib.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.mambo.lib.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO : BaseDAO<TaskEntity> {

    @Query("SELECT * FROM tasks")
    fun fetchAll(): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    fun fetchAllFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id ")
    fun fetchById(id: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE id = :id ")
    fun fetchByIdFlow(id: Int): Flow<TaskEntity?>

}
